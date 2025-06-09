package robos.missao;

// No pacote de missoes
import ambiente.Ambiente;
import ambiente.CentralComunicacao;
import interfaces.Comunicavel;
import robos.aereos.DroneVigilancia;
import robos.geral.Robo;
import java.util.ArrayList;
import java.util.List;

public class MissaoPatrulhaAerea implements Missao {
    private List<int[]> waypoints;
    private Comunicavel base; // Um robô ou entidade para receber o relatório

    public MissaoPatrulhaAerea(List<int[]> waypoints, Comunicavel base) {
        this.waypoints = waypoints;
        this.base = base;
    }

    @Override
    public void executar(Robo robo, Ambiente ambiente) {
        if (robo instanceof DroneVigilancia) {
            DroneVigilancia drone = (DroneVigilancia) robo;
            System.out.println("Iniciando missão: Patrulha Aérea.");
            List<Robo> robosDetectados = new ArrayList<>();

            try {
                for (int[] waypoint : waypoints) {
                    System.out.println("Movendo para waypoint: (" + waypoint[0] + ", " + waypoint[1] + ", " + waypoint[2] + ")");
                    drone.mover(waypoint[0], waypoint[1], waypoint[2], ambiente);
                    
                    ArrayList<Robo> encontradosNaArea = drone.identificarRobo();
                    for (Robo r : encontradosNaArea) {
                        if (!robosDetectados.contains(r) && r != drone) {
                            robosDetectados.add(r);
                        }
                    }
                }
                
                String relatorio = "Patrulha concluída. Robôs detectados: ";
                for(Robo r : robosDetectados) {
                    relatorio += r.getNome() + " ";
                }
                
                drone.enviarMensagem(base, relatorio, new CentralComunicacao());
                System.out.println("Relatório enviado para a base.");

            } catch (Exception e) {
                System.err.println("Falha na missão de patrulha: " + e.getMessage());
            }
        }
    }
}
