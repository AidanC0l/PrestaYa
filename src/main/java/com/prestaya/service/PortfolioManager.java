package main.java.com.prestaya.service;

import main.java.com.prestaya.model.Loan;
import main.java.com.prestaya.model.SocialHousingLoan;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PortfolioManager {
    
    //Se deifne la cantidad maxima de 10 creditos en la cartera
    private static final int MAX_LOANS = 10;

    //Se crea el arreglo de creditos
    private final Loan[] loans;
    private int loanCount;

    //Se inicializa la cartera con el constructor de la clase
    public PortfolioManager() {
        
        this.loans = new Loan[MAX_LOANS];

        this.loanCount = 0;
    }

    //Se registra un credito en la cartera
    public void registerLoan(Loan loan) {

        //Se valida la existencia del credito
        if (loan == null) {
            throw new IllegalArgumentException("No se puede registrar un crédito nulo");
        }
        
        //Se comprueba la capacidad maxima
        if (loanCount >= MAX_LOANS) {
            throw new IllegalStateException("La cartera ha alcanzado su capacidad máxima");
        }

        //Se evita los id duplicados
        for (int i = 0; i < loanCount; i++) {
            if(loans[i].getLoanId().equals(loan.getLoanId())) {
                throw new IllegalArgumentException("Ya existe un crédito con el identificador " + loan.getLoanId());
            }
        }

        //Se calcula la cuota y se registra el credito
        loan.calculateMonthlyInstallment();

        loans[loanCount] = loan;
        loanCount++;

        System.out.println("Crédito " + loan.getLoanId() + " registrado correctamente.");

    }

    //Se usa para consultar la cartera de creditos
    public void showPortfolio() {

        //Se comprueba si la cartera esta vacia
        if (loanCount == 0) {
            System.out.println("La cartera todavía no tiene créditos registrados.");
            return;
        }

        System.out.println();
        System.out.println("========== CARTERA PRESTAYA ==========");

        //Se muestran los creditos
        for (int i = 0; i < loanCount; i++) {
        System.out.println();
        System.out.println(loans[i]);
        }

        System.out.println("======================================");
        System.out.println("Total de créditos: " + loanCount);
        //Se muestra el total
        System.out.println("Capital total colocado: $" + calculateTotalPlacedAmount()
        );
    
    }

    //Este metodo suma el capital financiado de todos los creditos registrados en la cartera
    public BigDecimal calculateTotalPlacedAmount() {

        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i < loanCount; i ++) {
            
            BigDecimal financedAmount;

            //Comprueba si el objeto es una instancia de SocialHousingLoan y si lo es se usa la variable housingLoan
            if (loans[i] instanceof SocialHousingLoan housingLoan) {
                financedAmount = housingLoan.getFinancedAmount();
            } else {
                financedAmount = loans[i].getAmount();
            }

            //Se suma el capital del credito actual al total acumulado
            total = total.add(financedAmount);

        }

        //Devuelve el resultado con dos posiciones decimales y el redondeo
        return total.setScale(2, RoundingMode.HALF_UP);

    }

    //Este metodo devuelve el numero de creditos registrados
    public int getLoanCount() {
        return loanCount;
    }

}
