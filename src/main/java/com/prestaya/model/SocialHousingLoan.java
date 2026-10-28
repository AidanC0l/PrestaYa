package main.java.com.prestaya.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

//Hereda de Loan
public class SocialHousingLoan extends Loan {
    
    //Se define la tasa mensual
    private static final BigDecimal MONTHLY_RATE = new BigDecimal("0.008");

    //Se guarda el subsidio
    private final BigDecimal governmentSubsidieAmount;

    //Se realiza el constructuor para validar el subsidio
    public SocialHousingLoan(String loanId, BigDecimal amount, int termMonths, BigDecimal governmentSubsidieAmount) {

        super(loanId, amount, termMonths, MONTHLY_RATE);

        if (governmentSubsidieAmount == null || governmentSubsidieAmount.compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException ("El subsidio no puede ser nulo ni negativo.");

        }

        if (governmentSubsidieAmount.compareTo(amount) <= 0) {
        
            throw new IllegalArgumentException("El subsidio debe ser menor que el capital inicial");
        
        }

        this.governmentSubsidieAmount = governmentSubsidieAmount.setScale(2,RoundingMode.HALF_UP);

    }

    public BigDecimal getGovernmentSubsidieAmount() {
        
        return governmentSubsidieAmount;
    
    }

    //Se descuenta el subsidio del capital
    public BigDecimal getFinancedAmount() {
        
        return getAmount().subtract(governmentSubsidieAmount).setScale(2, RoundingMode.HALF_UP);

    }

    //Se implementa la formula mediante BigDecimal C={P*i*(1+i)^n} / {(1+i)^n-1}
    //C:cuota mensual, P:capital financiado despues del subsidio, i:tasa de interes mensual y n:numero de meses
    @Override
    public BigDecimal calculateMonthlyInstallment() {

        BigDecimal principal = getFinancedAmount();
        BigDecimal rate = getInterestRate();
        int months = getTermMonths();

        BigDecimal onePlusRate = BigDecimal.ONE.add(rate);
        BigDecimal power = onePlusRate.pow(months);

        BigDecimal numerator = principal
                .multiply(rate)
                .multiply(power);

        BigDecimal denominator = power.subtract(BigDecimal.ONE);

        BigDecimal installment = numerator.divide(
                denominator,
                2,
                RoundingMode.HALF_UP
        );

        setMonthlyInstallment(installment);

        return getMonthlyInstallment();
    }

    @Override
    public String toString() {
        return """
                Tipo de crédito: Vivienda de interés social
                Identificador: %s
                Capital inicial: $%s
                Subsidio gubernamental: $%s
                Capital financiado: $%s
                Plazo: %d meses
                Tasa mensual: %s%%
                Cuota mensual: $%s
                """.formatted(
                getLoanId(),
                getAmount().toPlainString(),
                governmentSubsidieAmount.toPlainString(),
                getFinancedAmount().toPlainString(),
                getTermMonths(),
                getInterestRate().multiply(
                        new BigDecimal("100")
                ).stripTrailingZeros().toPlainString(),
                getMonthlyInstallment() == null
                        ? "Pendiente de cálculo"
                        : getMonthlyInstallment().toPlainString()
        );
    }

}
