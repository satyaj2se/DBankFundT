package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.UpdateAccount;
import com.db.awmd.challenge.domain.FundTransfer;
import com.db.awmd.challenge.exception.NoAccountFoundException;
import com.db.awmd.challenge.exception.NotEnoughBalanceException;
import com.db.awmd.challenge.exception.SameAccountFundTransferException;
import com.db.awmd.challenge.repository.AccountsRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;

//Can act as on all Banking Service for Transfer the Funds.
@Service
public class FundTransferService implements IMoneyTransfer {

	@Getter
	private final AccountsRepository accountsRepository;

	@Getter
	private final NotificationService notificationService;

	@Autowired
	private IMoneyTransfer iMoneyTransfer;

	@Autowired
	public FundTransferService(AccountsRepository accountsRepository, NotificationService notificationService) {
		this.accountsRepository = accountsRepository;
		this.notificationService = notificationService;
	}

	/**
	 * Added functionality for the fund-transfer
	 */
	public void doTransfer(FundTransfer transfer)
			throws NoAccountFoundException, NotEnoughBalanceException, SameAccountFundTransferException {

		final Account accountFrom = accountsRepository.getAccount(transfer.getAccountFromId());
		final Account accountTo = accountsRepository.getAccount(transfer.getAccountToId());
		final BigDecimal amount = transfer.getAmount();

		iMoneyTransfer.fundTransferCheck(accountFrom, accountTo, transfer);

		boolean successful = accountsRepository
				.updateAccounts(Arrays.asList(new UpdateAccount(accountFrom.getAccountId(), amount.negate()),
						new UpdateAccount(accountTo.getAccountId(), amount)));

		if (successful) {
			notificationService.notifyAboutTransfer(accountFrom, "The transfer to the account...From "
					+ accountTo.getAccountId() + " is now complete for the amount of " + transfer.getAmount() + ".");
			notificationService.notifyAboutTransfer(accountTo, "Amount to Transferred...To + "
					+ accountFrom.getAccountId() + " has transferred " + transfer.getAmount() + " into your account.");
		}
	}

	// Fund Transfer Checks for ongoing web service call
	public void fundTransferCheck(final Account currAccountFrom, final Account currAccountTo,
			final FundTransfer transfer)
			throws NoAccountFoundException, NotEnoughBalanceException, SameAccountFundTransferException {

		if (currAccountFrom == null) {
			throw new NoAccountFoundException("Account " + transfer.getAccountFromId() + " not found.");
		}

		if (currAccountTo == null) {
			throw new NoAccountFoundException("Account " + transfer.getAccountToId() + " not found.");
		}

		if (sameAccountIDCheck(transfer)) {
			throw new SameAccountFundTransferException("Transfer to self not permisable.");
		}

		if (!balanceCheck(currAccountFrom, transfer.getAmount())) {
			throw new NotEnoughBalanceException("No sufficient Balance to transfer account "
					+ currAccountFrom.getAccountId() + " balance=" + currAccountFrom.getBalance());
		}
	}

	private boolean sameAccountIDCheck(final FundTransfer transfer) {
		return transfer.getAccountFromId().equals(transfer.getAccountToId());
	}

	private boolean balanceCheck(final Account account, final BigDecimal amount) {
		return account.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) >= 0;
	}

}
