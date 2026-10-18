package main.java.com.prestaya.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

//Se usa el extends ya que hereda de Loan
public class EntrepreneurLoan extends Loan {

    //Se declara la tasa preferencial
    private static final BigDecimal PREFERENTIAL_RATE= new BigDecimal("0.012");

    //Se crea el constructor del credito para emprendedores y se usa el super() para llamar el constructor de Loan
    public EntrepreneurLoan(String loanId, BigDecimal amount, int termMonths) {
        
        super(loanId, amount, termMonths, PREFERENTIAL_RATE);

    }

//Se sobreescribe el metodo abstracto y se implementa el comportamiento del credito para emprendedores
    @Override
    public BigDecimal calculateMonthlyInstallment() {

        //Se obtiene los datos necesarios para el calculo
        BigDecimal principal = getAmount();
        BigDecimal rate = getInterestRate();
        int months = getTermMonths();

        BigDecimal installment;

        if (rate.compareTo(BigDecimal.ZERO) == 0) {

            installment = principal.divide(BigDecimal.valueOf(months),2,RoundingMode.HALF_UP);
        
        } else {

            //Se suma 1 a la tasa
            BigDecimal onePlusRate = BigDecimal.ONE.add(rate);
            //Se eleva el resultado al numero de meses
            BigDecimal power = onePlusRate.pow(months);
            //Se calcula el numerador
            BigDecimal numerator = principal.multiply(rate).multiply(power);
            //Se calcula el denominador
            BigDecimal denominator = power.subtract(BigDecimal.ONE);
            //Se divide y redondea
            installment = numerator.divide(denominator, 2, RoundingMode.HALF_UP);

        }

        //Se guarda el resultado y se retorna la cuota
        setMonthlyInstallment(installment);

        return getMonthlyInstallment();

    }
    
}
