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

//Can act as on all Banking Account Services
@Service
public class AccountsService {

	@Getter
	private final AccountsRepository accountsRepository;

	@Getter
	private final NotificationService notificationService;

	@Autowired
	public AccountsService(AccountsRepository accountsRepository, NotificationService notificationService) {
		this.accountsRepository = accountsRepository;
		this.notificationService = notificationService;
	}

	public void createAccount(Account account) {
		this.accountsRepository.createAccount(account);
	}
	
	String sql = " INSERT INTO TABLE_(name,email,phone,id) VALUES(?,?,?,?) ";


try { 
        BufferedReader bReader = new BufferedReader(new FileReader("1000rows.csv"));
        String line = ""; 
        while ((line = bReader.readLine()) != null) {
            try {

                if (line != null) 
                {
                    String[] array = line.split(",+");
                    for(String result:array)
                    {
                        System.out.println(result);
 //Create preparedStatement here and set them and excute them
                PreparedStatement ps = yourConnecionObject.createPreparedStatement(sql);
                 ps.setString(1,str[0]);
                 ps.setString(2,str[1]);
                 ps.setString(3,str[2]);
                 ps.setString(4,strp[3])
                 ps.excuteUpdate();
                 ps. close()
   //Assuming that your line from file after split will folllow that sequence

                    }
                } 
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            finally
            {
               if (bReader == null) 
                {
                    bReader.close();
                }
            }
        }
    } catch (FileNotFoundException ex) {
        ex.printStackTrace();
    }

	public Account getAccount(String accountId) {
		return this.accountsRepository.getAccount(accountId);
	}
}
