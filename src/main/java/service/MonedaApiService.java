package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import models.ExchangeRateResponse;

public class MonedaApiService {

    private static final String API_KEY = "c0d859182cfea576de62ca2f";
    private static final String URL_BASE = "https://v6.exchangerate-api.com/v6/c0d859182cfea576de62ca2f/latest/USD";

    Scanner sca = new Scanner(System.in);

    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public ExchangeRateResponse requestApi() {

        String jsonResponse;
        ExchangeRateResponse tipoCambio = null;

        try {
            HttpClient cliente = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(MonedaApiService.URL_BASE))
                    .build();

            HttpResponse<String> response = cliente.send(request, HttpResponse.BodyHandlers.ofString());
            jsonResponse = response.body();

            tipoCambio = gson.fromJson(jsonResponse, ExchangeRateResponse.class);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return tipoCambio;
    }

    public void menu() {
        String opcion = "true";

        while (true) {

            if (opcion.contains("false")) {
                break;
            }

            System.out.println("********************************* ");
            System.out.println("Sea bienvenido/a al conversor de Monedas\b");

            System.out.println("1) Dólar =>> Peso Argentino.");
            System.out.println("2) Peso Argentino =>> Dólar.");
            System.out.println("3) Dólar =>> Real Brasileño.");
            System.out.println("4) Real Brasileño =>> Dólar.");
            System.out.println("5) Dólar =>> Peso Colombiano.");
            System.out.println("6) Peso Colombiano =>> Dólar.");
            System.out.println("7) Salir.");

            opcion = seleccionarOpcion();

            System.out.println("********************************* ");
        }

    }

    public String seleccionarOpcion() {
        int opcion;

        try {
            System.out.println("Elija una opción válida: ");
            opcion = sca.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Entrada no válida. Por favor, ingrese un número.");
            return "Entrada no válida.";
        }

        String message = switch (opcion) {
            case 1 -> {
                System.out.println("********************************* ");
                dolarAPesoArgentino();
                yield "Opción 1 seleccionada: Convertir de Dólar a Peso Argentino.";
            }
            case 2 -> {
                System.out.println("********************************* ");
                pesoArgentinoADOlar();
                yield "Opción 2 seleccionada: Convertir de Peso Argentino a Dólar.";
            }
            case 3 -> {
                System.out.println("********************************* ");
                dolarARealBrasileno();
                yield "Opción 3 seleccionada: Convertir de Dólar a Real Brasileño.";
            }
            case 4 -> {
                System.out.println("********************************* ");
                realBrasilenoADolar();
                yield "Opción 4 seleccionada: Convertir de Real Brasileño a Dólar.";
            }
            case 5 -> {
                System.out.println("********************************* ");
                dolarAPesoColombiano();
                yield "Opción 5 seleccionada: Convertir de Dólar a Peso Colombiano.";
            }
            case 6 -> {
                System.out.println("********************************* ");
                pesoColombianoADolar();
                yield "Opción 6 seleccionada: Convertir de Peso Colombiano a Dólar.";
            }
            case 7 -> {
                System.out.println("********************************* ");
                yield "false";
            }
            default -> {
                System.out.println("Opción no válida.");
                yield "Opción no válida.";
            }
        };

        return message;
    }

    public void dolarAPesoArgentino() {
        ExchangeRateResponse tipoCambio = requestApi();

        if (tipoCambio == null) {
            System.err.println("No se pudo obtener los tipos de cambio. Inténtalo más tarde.");
            return;
        }

        JsonObject conversionRates = gson.toJsonTree(tipoCambio).getAsJsonObject().getAsJsonObject("conversion_rates");

        double usd;
        double ars;

        System.out.println("Ingresa el valor de Dólares que deseas convertir: ");
        double valorAConvertir = sca.nextDouble();

        try {
            usd = conversionRates.get("USD").getAsDouble();
            ars = conversionRates.get("ARS").getAsDouble();
            System.out.printf(" %.2f [USD] equivale a %.2f [ARS]%n", valorAConvertir, (ars / usd) * valorAConvertir);
        } catch (Exception e) {
            System.err.println("Error en la conversión: " + e.getMessage());
        }

    }

    public void pesoArgentinoADOlar() {
        ExchangeRateResponse tipoCambio = requestApi();

        if (tipoCambio == null) {
            System.err.println("No se pudo obtener los tipos de cambio. Inténtalo más tarde.");
            return;
        }

        JsonObject conversionRates = gson.toJsonTree(tipoCambio).getAsJsonObject().getAsJsonObject("conversion_rates");

        double ars, usd;

        try {
            ars = conversionRates.get("ARS").getAsDouble();
            usd = conversionRates.get("USD").getAsDouble();
        } catch (Exception e) {
            System.err.println("Error al obtener las tasas de conversión: " + e.getMessage());
            return;
        }

        Scanner sca = new Scanner(System.in);
        System.out.println("Ingresa el valor de Pesos Argentinos que deseas convertir: ");

        try {
            double valorAConvertir = sca.nextDouble();
            System.out.printf("%.2f [ARS] equivale a %.2f [USD]%n", valorAConvertir, valorAConvertir / ars);
        } catch (InputMismatchException e) {
            System.err.println("Por favor, ingresa un valor numérico válido.");
        }
    }

    public void dolarARealBrasileno() {
        ExchangeRateResponse tipoCambio = requestApi();

        if (tipoCambio == null) {
            System.err.println("No se pudo obtener los tipos de cambio. Inténtalo más tarde.");
            return;
        }

        JsonObject conversionRates = gson.toJsonTree(tipoCambio).getAsJsonObject().getAsJsonObject("conversion_rates");

        double brl;

        try {
            brl = conversionRates.get("BRL").getAsDouble();
        } catch (Exception e) {
            System.err.println("Error al obtener la tasa de conversión: " + e.getMessage());
            return;
        }

        Scanner sca = new Scanner(System.in);
        System.out.println("Ingresa el valor de Dólares que deseas convertir: ");

        try {
            double valorAConvertir = sca.nextDouble();
            System.out.printf("%.2f [USD] equivale a %.2f [BRL]%n", valorAConvertir, brl * valorAConvertir);
        } catch (InputMismatchException e) {
            System.err.println("Por favor, ingresa un valor numérico válido.");
        }
    }

    public void realBrasilenoADolar() {
        ExchangeRateResponse tipoCambio = requestApi();

        if (tipoCambio == null) {
            System.err.println("No se pudo obtener los tipos de cambio. Inténtalo más tarde.");
            return;
        }

        JsonObject conversionRates = gson.toJsonTree(tipoCambio).getAsJsonObject().getAsJsonObject("conversion_rates");

        double brl;

        try {
            brl = conversionRates.get("BRL").getAsDouble();
        } catch (Exception e) {
            System.err.println("Error al obtener la tasa de conversión: " + e.getMessage());
            return;
        }

        Scanner sca = new Scanner(System.in);
        System.out.println("Ingresa el valor de Reales que deseas convertir: ");

        try {
            double valorAConvertir = sca.nextDouble();
            System.out.printf("%.2f [BRL] equivale a %.2f [USD]%n", valorAConvertir, valorAConvertir / brl);
        } catch (InputMismatchException e) {
            System.err.println("Por favor, ingresa un valor numérico válido.");
        }
    }

    public void dolarAPesoColombiano() {
        ExchangeRateResponse tipoCambio = requestApi();

        if (tipoCambio == null) {
            System.err.println("No se pudo obtener los tipos de cambio. Inténtalo más tarde.");
            return;
        }

        JsonObject conversionRates = gson.toJsonTree(tipoCambio).getAsJsonObject().getAsJsonObject("conversion_rates");

        double cop;

        try {
            cop = conversionRates.get("COP").getAsDouble();
        } catch (Exception e) {
            System.err.println("Error al obtener la tasa de conversión: " + e.getMessage());
            return;
        }

        Scanner sca = new Scanner(System.in);
        System.out.println("Ingresa el valor de Dólares que deseas convertir: ");

        try {
            double valorAConvertir = sca.nextDouble();
            System.out.printf("%.2f [USD] equivale a %.2f [COP]%n", valorAConvertir, cop * valorAConvertir);
        } catch (InputMismatchException e) {
            System.err.println("Por favor, ingresa un valor numérico válido.");
        }
    }

    public void pesoColombianoADolar() {
        ExchangeRateResponse tipoCambio = requestApi();

        if (tipoCambio == null) {
            System.err.println("No se pudo obtener los tipos de cambio. Inténtalo más tarde.");
            return;
        }

        JsonObject conversionRates = gson.toJsonTree(tipoCambio).getAsJsonObject().getAsJsonObject("conversion_rates");

        double cop;

        try {
            cop = conversionRates.get("COP").getAsDouble();
        } catch (Exception e) {
            System.err.println("Error al obtener la tasa de conversión: " + e.getMessage());
            return;
        }

        Scanner sca = new Scanner(System.in);
        System.out.println("Ingresa el valor de Pesos Colombianos que deseas convertir: ");

        try {
            double valorAConvertir = sca.nextDouble();
            System.out.printf("%.2f [COP] equivale a %.2f [USD]%n", valorAConvertir, valorAConvertir / cop);
        } catch (InputMismatchException e) {
            System.err.println("Por favor, ingresa un valor numérico válido.");
        }
    }

}
