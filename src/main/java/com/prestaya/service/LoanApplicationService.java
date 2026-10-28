package main.java.com.prestaya.service;

import main.java.com.prestaya.model.ApplicationStatus;
import main.java.com.prestaya.model.ApprovedStatus;
import main.java.com.prestaya.model.Customer;
import main.java.com.prestaya.model.Loan;
import main.java.com.prestaya.model.RejectedStatus;
import main.java.com.prestaya.model.SocialHousingLoan;

import java.math.BigDecimal;

public class LoanApplicationService {
    
    //Se define el puntaje minimo
    private static final int MINIMUM_CREDIT_SCORE = 600;

    //Se crea el metodo de evaluacion
    public ApplicationStatus evaluateApplication(Customer customer, Loan loan) {

        if (customer == null) {
            throw new IllegalArgumentException(
                "El cliente es obligatorio para evaluar la solicitud."
            );
        }

        if (loan == null) {
            throw new IllegalArgumentException("El crédito es obligatorio para evaluar la solicitud.");
        }

        //se aplica la regla del puntaje
        if (customer.creditScore() < MINIMUM_CREDIT_SCORE) {
            return new RejectedStatus("Puntaje crediticio inferior al mínimo requerido de "+ MINIMUM_CREDIT_SCORE + " puntos.");
        }

        //Se determina el monto aprobado
        BigDecimal approvedAmount;

        if (loan instanceof SocialHousingLoan housingLoan) {
            //Para el credito de vivienda
            approvedAmount = housingLoan.getFinancedAmount();
        } else {
            //Para el credito empresarial
            approvedAmount = loan.getAmount();
        }

        //Se calcula la cuota mensual
        BigDecimal monthlyInstallment = loan.calculateMonthlyInstallment();

        return new ApprovedStatus(approvedAmount, monthlyInstallment);
    }

}
