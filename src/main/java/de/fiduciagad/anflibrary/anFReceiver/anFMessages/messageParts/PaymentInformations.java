package de.fiduciagad.anflibrary.anFReceiver.anFMessages.messageParts;

import android.content.Context;

import de.fiduciagad.anflibrary.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Vorbereitung für das Darstellen von weiteren Informtationen durch das Framework.
 * In diesem Fall Informationen zu Zahlungsmitteln
 */
public class PaymentInformations extends MessagePart {

    private JSONObject accountAction;
    private JSONArray paymentInformation;
    private JSONObject from;

    private String subject;
    private Double balance;
    private String iban;
    private String bic;
    private String name;
    private List<String> currencyAvailable;

    public PaymentInformations(JSONObject anfObject, Context context) {
        super(anfObject, context);
        fillPaymentInformations();
    }

    private void fillPaymentInformations() {

        accountAction = getJSONObject(R.string.accountActions, anfObject);
        paymentInformation = getJSONArray(R.string.paymentInformation, anfObject);

        fillCurrencyAvailable();

        subject = getJSONString(R.string.subject, accountAction);
        String balanceString = getJSONString(R.string.accountBalance, accountAction);
        balance = Double.parseDouble(balanceString);
        from = getJSONObject(R.string.from, accountAction);
        bic = getJSONString(R.string.bic, from);
        name = getJSONString(R.string.fromName, from);
        iban = getJSONString(R.string.iban, from);
    }

    private void fillCurrencyAvailable() {
        currencyAvailable = new ArrayList<>();
        for (int i = 0; i < paymentInformation.length(); i++) {
            try {
                currencyAvailable.add(paymentInformation.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isValid() {
        if (accountAction != null) {
            return isValidAccountAction();
        } else if (paymentInformation != null) {
            return isValidCurrency();
        }
        return false;
    }

    private boolean isValidAccountAction() {

        if (subject != null && balance != null && from != null) {
            return isValidFrom();
        }
        return false;
    }

    private boolean isValidFrom() {
        if (iban != null && bic != null && name != null) {
            return true;
        }
        return false;
    }

    private boolean isValidCurrency() {
        if (currencyAvailable != null) {
            for (String currency : currencyAvailable) {
                if (!getCurrencyList().contains(currency)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private List<String> getCurrencyList() {
        String[] systemTypes = res.getStringArray(R.array.curreny_values);
        List<String> currencyList = Arrays.asList(systemTypes);
        return currencyList;
    }

    public String getSubject() {
        return subject;
    }

    public Double getBalance() {
        return balance;
    }

    public String getIban() {
        return iban;
    }

    public String getBic() {
        return bic;
    }

    public String getName() {
        return name;
    }

    public List<String> getCurrencyAvailable() {
        return currencyAvailable;
    }
}
