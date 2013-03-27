/**
 * Contains all languages supported by the server
 */
package server;

import java.util.*;

import ATMCommon.data.Language;

public class Languages {
	private static HashMap<Integer, Language> languages = new HashMap<Integer, Language>();
	
	/**
	 * Init all languages
	 */
	public static void init()
	{
		initEng();
		initSwe();
	}
	
	/**
	 * Init English
	 */
	private static void initEng()
	{				
		Language lang = new Language();
		lang.put("login", "Please login with your card number and pin");
		lang.put("loginuser", "Enter card number:");
		lang.put("loginpass", "Enter pin:");
		lang.put("loginfail", "Login failed");
		lang.put("options", "(0)Logout\n(1)Balance\n(2)Withdrawal\n(3)Deposit\n(4)Select language\n");
		lang.put("welcome", String.format("Welcome to Bank! %s", lang.get("options")));		
		lang.put("enteramount", "Enter amount: ");
		lang.put("currentamount", "Current balance is %i dollars");
		lang.put("bye", "Good Bye");
		lang.put("nolang", "No such language");
		lang.put("invalidopt", "Invalid option");
		lang.put("langselect", "(0)Back\n(1)English\n(2)Swedish\n");
		lang.put("balance", "Current Balance: ");
		lang.put("withdrawalamount", "Enter amount to withdraw:");
		lang.put("withdrawalcode", "Enter single use code for withdrawal:");
		lang.put("withdrawalsucc", "Withdrawal was successfull");
		lang.put("withdrawalfail", "Withdrawal failed. Not enough balance or invalid code");
		lang.put("depositamount", "Enter amount to deposit:");
		lang.put("depositcomplete", "Deposit was completed");
		
		languages.put(1, lang);
	}
	
	/**
	 * Init Swedish
	 */
	private static void initSwe()
	{
		Language lang = new Language();
		lang.put("login", "Vänligen logga in med ditt kortnummer och sifferkod");
		lang.put("loginuser", "Ange kortnummer:");
		lang.put("loginpass", "Ange sifferkod:");
		lang.put("loginfail", "Inloggningen misslyckades");
		lang.put("options", "(0)Logga ut\n(1)Saldo\n(2)Uttag\n(3)Insättning\n(4)Välj språk\n");
		lang.put("welcome", String.format("Välkommen till banken! %s", lang.get("options")));		
		lang.put("enteramount", "Ange summa: ");
		lang.put("currentamount", "Nuvarande saldo %i dollar");
		lang.put("bye", "Hejdå");
		lang.put("nolang", "Inget sådant språk");
		lang.put("invalidopt", "Ogiltigt alternativ");
		lang.put("langselect", "(0)Tillbaka\n(1)Engelska\n(2)Svenska\n");
		lang.put("balance", "Saldo: ");
		lang.put("withdrawalamount", "Ange summa:");
		lang.put("withdrawalcode", "Ange engångskod för uttag:");
		lang.put("withdrawalsucc", "Uttag lyckades");
		lang.put("withdrawalfail", "Uttag misslyckades. Otillräckligt saldo eller ogiltig engångskod");
		lang.put("depositamount", "Ange summa:");
		lang.put("depositcomplete", "Insättning slutförd");
		
		languages.put(2, lang);
	}
	
	/**
	 * Get language with provided ID
	 */
	public static Language getLanguage(int langId)
	{
		return languages.get(langId);
	}
}
