import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SistemaEnvios {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        HashMap<String, Envio> enTransito = new HashMap<>();
        ArrayList<Envio> entregados = new ArrayList<>();
        int contador = 1001;
        String opcion;

        do {
            System.out.println("\n===== SERVIENTREGA =====");
            System.out.println("1. Registrar envío");
            System.out.println("2. Consultar envío");
            System.out.println("3. Entregar envío");
            System.out.println("4. Listar entregados");
            System.out.println("5. Salir");
            System.out.print("Opción: ");
            opcion = sc.nextLine();

            switch (opcion) {
                case "1":
                    System.out.print("Servicio (1 nacional, 2 express, 3 internacional): ");
                    int tipo = Integer.parseInt(sc.nextLine());
                    System.out.print("Destinatario: ");
                    String destinatario = sc.nextLine();
                    System.out.print("Ciudad de destino: ");
                    String ciudad = sc.nextLine();
                    System.out.print("Peso en kg: ");
                    double peso = Double.parseDouble(sc.nextLine());

                    if (peso <= 0) {
                        System.out.println("El peso debe ser mayor que cero. Envío no registrado.");
                        break;
                    }

                    Envio envio = null;
                    String numero = String.valueOf(contador);

                    if (tipo == 1 || tipo == 2) {
                        System.out.print("¿Entrega a domicilio? (S/N): ");
                        boolean domicilio = sc.nextLine().equalsIgnoreCase("S");
                        if (tipo == 1) {
                            envio = new EnvioNacional("NAC" + numero, destinatario, ciudad, peso, domicilio);
                        } else {
                            System.out.print("¿El cliente eligió 24 horas? (S/N): ");
                            boolean veinticuatro = sc.nextLine().equalsIgnoreCase("S");
                            if (veinticuatro) {
                                envio = new EnvioExpress("EXP" + numero, destinatario, ciudad, peso, domicilio, 24);
                            } else {
                                envio = new EnvioExpress("EXP" + numero, destinatario, ciudad, peso, domicilio);
                            }
                        }
                    } else if (tipo == 3) {
                        System.out.print("Valor de la compra (USD): ");
                        double valor = Double.parseDouble(sc.nextLine());
                        EnvioInternacional inter =
                                new EnvioInternacional("INT" + numero, destinatario, ciudad, peso, valor);
                        if (!inter.cumple4x4()) {
                            System.out.println("Envío rechazado: no cumple la regla 4x4 de aduana.");
                            break;
                        }
                        envio = inter;
                    } else {
                        System.out.println("Servicio inválido.");
                        break;
                    }

                    enTransito.put(envio.getGuia(), envio);
                    contador++;
                    System.out.println("Envío registrado. Guía: " + envio.getGuia());
                    System.out.printf("Costo: $%.2f%n", envio.calcularCosto());
                    break;

                case "2":
                    System.out.print("Guía: ");
                    String guiaC = sc.nextLine();
                    if (enTransito.containsKey(guiaC)) {
                        Envio e = enTransito.get(guiaC);
                        System.out.println(e.describir());
                        System.out.printf("Costo: $%.2f%n", e.calcularCosto());
                    } else {
                        System.out.println("No existe un envío en tránsito con esa guía.");
                    }
                    break;

                case "3":
                    System.out.print("Guía: ");
                    String guiaE = sc.nextLine();
                    if (enTransito.containsKey(guiaE)) {
                        Envio e = enTransito.remove(guiaE);
                        entregados.add(e);
                        System.out.println("Envío " + guiaE + " entregado.");
                    } else {
                        System.out.println("No existe un envío en tránsito con esa guía.");
                    }
                    break;

                case "4":
                    if (entregados.isEmpty()) {
                        System.out.println("Todavía no hay envíos entregados.");
                    } else {
                        double total = 0;
                        for (Envio e : entregados) {
                            System.out.println(e.describir());
                            System.out.printf("Costo: $%.2f%n", e.calcularCosto());
                            total += e.calcularCosto();
                        }
                        System.out.printf("Total recaudado: $%.2f%n", total);
                    }
                    break;

                case "5":
                    System.out.println("Hasta luego.");
                    break;

                default:
                    System.out.println("Opción inválida");
            }
        } while (!opcion.equals("5"));

        sc.close();
    }
}
