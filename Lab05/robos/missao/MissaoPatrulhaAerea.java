package robos.missao;

// No pacote de missoes
import ambiente.Ambiente;
import ambiente.CentralComunicacao;
import interfaces.Comunicavel;
import robos.aereos.DroneVigilancia;
import robos.geral.Robo;
import utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class MissaoPatrulhaAerea implements Missao {
    private List<int[]> waypoints;
    private Comunicavel base; // Um robô ou entidade para receber o relatório
    private CentralComunicacao central;

    public MissaoPatrulhaAerea(List<int[]> waypoints, Comunicavel base) {
        this.waypoints = waypoints;
        this.base = base;
    }

    @Override
    public String executar(Robo robo, Ambiente ambiente, Logger logger) {
        this.central=ambiente.getCentral();
        if (robo instanceof DroneVigilancia) {
            DroneVigilancia drone = (DroneVigilancia) robo;
            String resultado ="MISSÃO: Patrulha Aérea.\n";
            List<Robo> robosDetectados = new ArrayList<>();

            try {
                for (int[] waypoint : waypoints) {
                    resultado+="Movendo para waypoint: (" + waypoint[0] + ", " + waypoint[1] + ", " + waypoint[2] + ")\n";
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
                
                drone.enviarMensagem(base, relatorio, central);
                resultado+="Relatório enviado para a base.";
                logger.escreverLogSucesso(resultado);

            } catch (Exception e) {
                resultado+="Falha na missão de patrulha: " + e.getMessage();
                try{
                    logger.escreverLogFalha(resultado);
                } catch(Exception ex){
                    resultado+="";
                }
            }
            return resultado;
        }
        return "Missão PatrulhaAerea só pode ser executada por um Drone de Vigilancia";
    }
}
