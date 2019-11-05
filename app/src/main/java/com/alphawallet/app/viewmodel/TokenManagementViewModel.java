package com.alphawallet.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.alphawallet.app.entity.Token;
import com.alphawallet.app.entity.Wallet;
import com.alphawallet.app.interact.ChangeTokenEnableInteract;
import com.alphawallet.app.repository.TokenRepositoryType;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TokenManagementViewModel extends BaseViewModel {
    private final TokenRepositoryType tokenRepository;
    private final ChangeTokenEnableInteract changeTokenEnableInteract;

    private final MutableLiveData<Token[]> tokens = new MutableLiveData<>();

    public TokenManagementViewModel(TokenRepositoryType tokenRepository,
                                    ChangeTokenEnableInteract changeTokenEnableInteract)
    {
        this.tokenRepository = tokenRepository;
        this.changeTokenEnableInteract = changeTokenEnableInteract;
    }

    public LiveData<Token[]> tokens()
    {
        return tokens;
    }

    public void fetchTokens(String walletAddr)
    {
        tokenRepository.fetchActiveStoredPlusEth(walletAddr)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onTokensFetched, this::onError);
    }

    private void onTokensFetched(Token[] tokenArray)
    {
        tokens.postValue(tokenArray);
    }

    public void setTokenEnabled(Wallet wallet, Token token, boolean enabled) {
        changeTokenEnableInteract.setEnable(wallet, token, enabled);
    }

}
