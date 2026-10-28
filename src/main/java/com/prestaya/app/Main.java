package main.java.com.prestaya.app;

import main.java.com.prestaya.model.ApplicationStatus;
import main.java.com.prestaya.model.ApprovedStatus;
import main.java.com.prestaya.model.Customer;
import main.java.com.prestaya.model.EntrepreneurLoan;
import main.java.com.prestaya.model.Loan;
import main.java.com.prestaya.model.PlatformStatus;
import main.java.com.prestaya.model.RejectedStatus;
import main.java.com.prestaya.model.SocialHousingLoan;
import main.java.com.prestaya.service.LoanApplicationService;
import main.java.com.prestaya.service.PortfolioManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    
    private static final Scanner SCANNER = new Scanner(System.in);

    private static final PortfolioManager PORTFOLIO =
            new PortfolioManager();

    private static final LoanApplicationService APPLICATION_SERVICE =
            new LoanApplicationService();

    private static PlatformStatus platformStatus =
            PlatformStatus.OPERATIONAL;

    public static void main(String[] args) {

        int option;

        do {
            showMenu();
            option = readOption();

            switch (option) {
                case 1 -> simulateApplication();
                case 2 -> approveAndRegisterLoan();
                case 3 -> PORTFOLIO.showPortfolio();
                case 4 -> showPlatformStatus();
                case 5 -> System.out.println(
                        "Gracias por utilizar PrestaYa Fintech."
                );
                default -> System.out.println("Opción inválida. Seleccione un número del 1 al 5.");
            }

            System.out.println();

        } while (option != 5);

        SCANNER.close();
    }

    private static void showMenu() {

        System.out.println("======================================");
        System.out.println("         PRESTAYA FINTECH");
        System.out.println(" Sistema de gestión de microcréditos");
        System.out.println("======================================");
        System.out.println("1. Simular solicitud de crédito");
        System.out.println("2. Aprobar y registrar crédito");
        System.out.println("3. Consultar cartera");
        System.out.println("4. Consultar estado de la plataforma");
        System.out.println("5. Salir");
        System.out.println("======================================");
        System.out.print("Seleccione una opción: ");
    }

    private static int readOption() {

        while (!SCANNER.hasNextInt()) {
            System.out.println("Debe ingresar un número entero.");
            SCANNER.nextLine();
            System.out.print("Seleccione una opción: ");
        }

        int option = SCANNER.nextInt();
        SCANNER.nextLine();

        return option;
    }

    private static void simulateApplication() {

        if (platformStatus != PlatformStatus.OPERATIONAL) {
            System.out.println("No es posible procesar solicitudes. Estado: " + platformStatus);
            return;
        }

        LoanApplicationData data = readApplicationData();

        ApplicationStatus result =
                APPLICATION_SERVICE.evaluateApplication(
                        data.customer(),
                        data.loan()
                );

        showApplicationResult(result);
    }

    private static void approveAndRegisterLoan() {

        if (platformStatus != PlatformStatus.OPERATIONAL) {
            System.out.println("No es posible registrar créditos. Estado: " + platformStatus);
            return;
        }

        LoanApplicationData data = readApplicationData();

        ApplicationStatus result = APPLICATION_SERVICE.evaluateApplication(data.customer(),data.loan());

        showApplicationResult(result);

        if (result instanceof ApprovedStatus) {
            PORTFOLIO.registerLoan(data.loan());
        }
    }

    private static LoanApplicationData readApplicationData() {

        System.out.println();
        System.out.println("--- Datos del cliente ---");

        System.out.print("Identificación: ");
        String customerId = SCANNER.nextLine().trim();

        System.out.print("Nombre completo: ");
        String customerName = SCANNER.nextLine().trim();

        int creditScore = readInteger(
                "Puntaje crediticio (0-1000): ",
                0,
                1000
        );

        System.out.print("Ciudad: ");
        String city = SCANNER.nextLine().trim();

        Customer customer = new Customer(customerId, customerName, creditScore, city);

        System.out.println();
        System.out.println("--- Datos del crédito ---");
        System.out.println("1. Crédito para emprendedores");
        System.out.println("2. Crédito de vivienda social");

        int loanType = readInteger(
                "Seleccione el tipo de crédito: ",
                1,
                2
        );

        BigDecimal amount = readPositiveDecimal(
                "Capital inicial solicitado (COP): "
        );

        int months = readInteger(
                "Plazo en meses: ",
                1,
                600
        );

        Loan loan;

        String loanId = UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();

        if (loanType == 1) {

            loan = new EntrepreneurLoan(
                    "EMP-" + loanId,
                    amount,
                    months
            );

        } else {

            BigDecimal subsidy = readNonNegativeDecimal(
                    "Subsidio gubernamental (COP): "
            );

            loan = new SocialHousingLoan(
                    "VIS-" + loanId,
                    amount,
                    months,
                    subsidy
            );
        }

        return new LoanApplicationData(customer, loan);
    }

    private static void showApplicationResult(ApplicationStatus result) {

        System.out.println();
        System.out.println("--- Resultado de la solicitud ---");

        switch (result) {
            case ApprovedStatus approved -> {
                System.out.println("Solicitud aprobada.");
                System.out.println("Monto aprobado: $" + approved.approvedAmount());
                System.out.println("Cuota mensual: $" + approved.calculatedInstallment());
            }

            case RejectedStatus rejected -> {
                System.out.println("Solicitud rechazada.");
                System.out.println("Motivo: " + rejected.rejectionReason());
            }
        }
    }

    private static void showPlatformStatus() {

        System.out.println();
        System.out.println("--- Estado de la plataforma ---");

        switch (platformStatus) {
            case OPERATIONAL ->
                    System.out.println("PrestaYa está operativa.");

            case UNDER_MAINTENANCE ->
                    System.out.println("PrestaYa está en mantenimiento.");

            case OUT_OF_SERVICE ->
                    System.out.println("PrestaYa está fuera de servicio.");
        }
    }

    private static int readInteger(String message, int minimum, int maximum
    ) {

        while (true) {
            System.out.print(message);

            if (SCANNER.hasNextInt()) {
                int value = SCANNER.nextInt();
                SCANNER.nextLine();

                if (value >= minimum && value <= maximum) {
                    return value;
                }

                System.out.printf("Ingrese un valor entre %d y %d.%n",minimum,maximum
                );

            } else {
                System.out.println("Debe ingresar un número entero.");
                SCANNER.nextLine();
            }
        }
    }

    private static BigDecimal readPositiveDecimal(String message) {

        while (true) {
            BigDecimal value = readDecimal(message);

            if (value.compareTo(BigDecimal.ZERO) > 0) {
                return value.setScale(2, RoundingMode.HALF_UP);
            }

            System.out.println("El valor debe ser mayor que cero.");
        }
    }

    private static BigDecimal readNonNegativeDecimal(String message) {

        while (true) {
            BigDecimal value = readDecimal(message);

            if (value.compareTo(BigDecimal.ZERO) >= 0) {
                return value.setScale(2, RoundingMode.HALF_UP);
            }

            System.out.println("El valor no puede ser negativo.");
        }
    }

    private static BigDecimal readDecimal(String message) {

        while (true) {
            System.out.print(message);

            String input = SCANNER.nextLine().trim()
                    .replace(",", ".");

            try {
                return new BigDecimal(input);

            } catch (NumberFormatException exception) {
                System.out.println("Ingrese un valor numérico válido, por ejemplo 1500000.00.");
            }
        }
    }

    private record LoanApplicationData(Customer customer,Loan loan) { }
}
