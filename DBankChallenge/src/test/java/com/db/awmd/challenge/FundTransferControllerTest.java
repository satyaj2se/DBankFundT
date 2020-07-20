package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.FundTransfer;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.FundTransferService;

import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class FundTransferControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private FundTransferService fundTransferService;

	@Autowired
	private AccountsService accountsService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void prepareMockMvc() {
		this.mockMvc = webAppContextSetup(this.webApplicationContext).build();

		accountsService.getAccountsRepository().clearAccounts();

	}

	// Valid Account to Account transfer test case execution
	@Test
	public void validTransferBetweenAccounts() throws Exception {
		final Account accountFrom = new Account("Axc007", new BigDecimal("400.00"));
		final Account accountTo = new Account("Axc009");
		final FundTransfer transfer = new FundTransfer("Axc007", "Axc009", new BigDecimal("200.00"));

		fundTransferService.fundTransferCheck(accountFrom, accountTo, transfer);
	}

	@Test
	public void createAccountNoAccountId() throws Exception {
		this.mockMvc.perform(post("/v1.0/accounts/fundtransfer").contentType(MediaType.APPLICATION_JSON)
				.content("{\"balance\":1000}")).andExpect(status().isBadRequest());
	}

	
	@Test
	public void performSameAccountTransferException() throws Exception {
		this.mockMvc
				.perform(post("/v1.0/accounts/fundtransfer").contentType(MediaType.APPLICATION_JSON)
						.content("{\"accountFromId\":\"ACC-001\",\"accountToId\":\"ACC-001\",\"balance\":3000}"))
				.andExpect(status().isBadRequest());

	}
}
