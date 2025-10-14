// Local: Adaptadores/Apresentacao/PedidoController.java
package com.luiza.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping; // Importar List
import org.springframework.web.bind.annotation.RestController;

import com.luiza.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.CancelarPedidoPresenter;
import com.luiza.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.PedidoPresenter;
import com.luiza.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.StatusPedidoPresenter;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.CancelarPedidoUC;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses.CancelarPedidoResponse;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses.StatusPedidoResponse;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses.SubmeterPedidoResponse;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.SolicitarStatusUC;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoUC;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoUC.ItemData;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private SubmeterPedidoUC submeterPedidoUC;
    private SolicitarStatusUC solicitarStatusUC;
    private CancelarPedidoUC cancelarPedidoUC;

    @Autowired
    public PedidoController(SubmeterPedidoUC submeterPedidoUC, 
                            SolicitarStatusUC solicitarStatusUC,
                            CancelarPedidoUC cancelarPedidoUC) {
        this.submeterPedidoUC = submeterPedidoUC;
        this.solicitarStatusUC = solicitarStatusUC;
        this.cancelarPedidoUC = cancelarPedidoUC;
    }

    @PostMapping("/{clienteCpf}")
    @CrossOrigin("*")
    public ResponseEntity<?> submetePedido(
            @PathVariable String clienteCpf, 
            @RequestBody List<ItemData> itensData) {
        
        try {
            
            SubmeterPedidoResponse response = submeterPedidoUC.run(clienteCpf, itensData);
            
            PedidoPresenter presenter = new PedidoPresenter(response.getPedido());

            return ResponseEntity.status(HttpStatus.CREATED).body(presenter);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao processar o pedido.");
        }
    }

    @GetMapping("/{id}/status")
    @CrossOrigin("*")
    public StatusPedidoPresenter recuperaStatus(@PathVariable long id) {
        StatusPedidoResponse response = solicitarStatusUC.run(id);
        if (response == null) return null; // pode trocar por 404 depois
        return new StatusPedidoPresenter(
            response.id(),
            response.status(),
            response.valorTotal(),
            response.desconto(),
            response.dataHoraPagamento()
        );
    } 

    @PutMapping("/{id}/cancelar")
    @CrossOrigin("*")
    public ResponseEntity<?> cancelarPedido(@PathVariable long id) {
        try {
            CancelarPedidoResponse response = cancelarPedidoUC.run(id);
            CancelarPedidoPresenter presenter = new CancelarPedidoPresenter(
                response.id(),
                response.status(),
                response.mensagem()
            );
            return ResponseEntity.ok(presenter);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao cancelar o pedido.");
        }
    } 
}
