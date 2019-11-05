package com.alphawallet.app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.alphawallet.app.R;
import com.alphawallet.app.entity.Token;
import com.alphawallet.app.entity.Wallet;
import com.alphawallet.app.ui.widget.adapter.TokenListAdapter;
import com.alphawallet.app.viewmodel.TokenManagementViewModel;
import com.alphawallet.app.viewmodel.TokenManagementViewModelFactory;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class TokenManagementActivity extends BaseActivity implements TokenListAdapter.ItemClickListener {
    @Inject
    TokenManagementViewModelFactory viewModelFactory;
    private TokenManagementViewModel viewModel;

    RecyclerView tokenList;
    Button saveButton;
    TokenListAdapter adapter;

    Token[] tokens;
    Wallet wallet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_management);
        toolbar();
        setTitle(R.string.empty);
        tokenList = findViewById(R.id.token_list);
        tokenList.setLayoutManager(new LinearLayoutManager(this));

        saveButton = findViewById(R.id.save_button);
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(TokenManagementViewModel.class);
        viewModel.tokens().observe(this, this::onTokens);

        if (getIntent() != null) {
            String walletAddr = getIntent().getStringExtra("wallet_address");
            wallet = new Wallet(walletAddr);
            viewModel.fetchTokens(walletAddr);
        } else {
            finish();
        }
    }

    private void onTokens(Token[] tokenArray)
    {
        if (tokenArray != null && tokenArray.length > 0) {
            adapter = new TokenListAdapter(this, tokenArray, this::onItemClick);
            tokenList.setAdapter(adapter);
        }
    }

    @Override
    public void onItemClick(Token token, boolean enabled)
    {
        viewModel.setTokenEnabled(wallet, token, enabled);
    }
}
