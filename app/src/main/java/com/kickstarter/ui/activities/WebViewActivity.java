package com.kickstarter.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.kickstarter.R;
import com.kickstarter.libs.BaseActivity;
import com.kickstarter.libs.qualifiers.RequiresViewModel;
import com.kickstarter.services.KSWebViewClient;
import com.kickstarter.ui.IntentKey;
import com.kickstarter.ui.toolbars.KSToolbar;
import com.kickstarter.ui.views.KSWebView;
import com.kickstarter.viewmodels.WebViewViewModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;

@RequiresViewModel(WebViewViewModel.class)
public final class WebViewActivity extends BaseActivity<WebViewViewModel> implements KSWebViewClient.Delegate {
  protected @Bind(R.id.web_view_toolbar) KSToolbar toolbar;
  protected @Bind(R.id.web_view) KSWebView webView;
  protected @Bind(R.id.checkout_loading_indicator) View loadingIndicatorView;

  @Override
  protected void onCreate(final @Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.web_view_layout);
    ButterKnife.bind(this);

    webView.client().setDelegate(this);

    viewModel.outputs.toolbarTitle()
      .compose(bindToLifecycle())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(toolbar::setTitle);

    viewModel.outputs.url()
      .compose(bindToLifecycle())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(webView::loadUrl);
  }

  @Override
  @OnClick(R.id.back_button)
  public void onBackPressed() {
    super.onBackPressed();
    overridePendingTransition(R.anim.fade_in_slide_in_left, R.anim.slide_out_right);
  }

  @Override
  public void webViewOnPageStarted(final @NonNull KSWebViewClient webViewClient, final @Nullable String url) {
    final AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
    animation.setDuration(300l);
    animation.setFillAfter(true);
    loadingIndicatorView.startAnimation(animation);
  }

  @Override
  public void webViewOnPageFinished(final @NonNull KSWebViewClient webViewClient, final @Nullable String url) {
    final AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
    animation.setDuration(300l);
    animation.setFillAfter(true);
    loadingIndicatorView.startAnimation(animation);
  }
}
