package com.luiza.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luiza.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.ListaPedidosPresenter;
import com.luiza.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.PedidoPresenter;
import com.luiza.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.StatusPedidoPresenter;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.CancelarPedidoUC;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.ListarPedidosEntreguesUC;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.PagarPedidoUC;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses.ListarPedidosEntreguesResponse;
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
    private PagarPedidoUC pagarPedidoUC;
    private ListarPedidosEntreguesUC listarPedidosEntreguesUC;

    @Autowired
    public PedidoController(SubmeterPedidoUC submeterPedidoUC, 
                            SolicitarStatusUC solicitarStatusUC,
                            CancelarPedidoUC cancelarPedidoUC,
                            PagarPedidoUC pagarPedidoUC,
                            ListarPedidosEntreguesUC listarPedidosEntreguesUC) {
        this.submeterPedidoUC = submeterPedidoUC;
        this.solicitarStatusUC = solicitarStatusUC;
        this.cancelarPedidoUC = cancelarPedidoUC;
        this.pagarPedidoUC = pagarPedidoUC;
        this.listarPedidosEntreguesUC = listarPedidosEntreguesUC;
    }

    //UC2
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

    //UC3
    @GetMapping("/{id}/status")
    @CrossOrigin("*")
    public ResponseEntity<?> recuperaStatus(@PathVariable long id) {
        try {
            StatusPedidoResponse response = solicitarStatusUC.run(id);
            StatusPedidoPresenter presenter = new StatusPedidoPresenter(
                response.id(),
                response.status(),
                response.valorTotal(),
                response.desconto(),
                response.dataHoraPagamento() 
            );
            return ResponseEntity.ok(presenter);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //UC4
    @PutMapping("/{id}/cancelar")
    @CrossOrigin("*")
    public ResponseEntity<?> cancelarPedido(@PathVariable long id) {
        try {
            SubmeterPedidoResponse response = cancelarPedidoUC.run(id);
            PedidoPresenter presenter = new PedidoPresenter(response.getPedido());
            
            return ResponseEntity.ok(presenter);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao cancelar o pedido.");
        }
    } 

    //UC5
    @PutMapping("/{id}/pagar")
    @CrossOrigin("*")
    public ResponseEntity<?> pagarPedido(@PathVariable long id) {
        try {
            SubmeterPedidoResponse response = pagarPedidoUC.run(id);
            PedidoPresenter presenter = new PedidoPresenter(response.getPedido());
            
            // Retorna 200 OK com o pedido atualizado
            return ResponseEntity.ok(presenter); 

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // Boa prática para debugar erros internos
            return ResponseEntity.internalServerError().body("Erro ao processar o pagamento.");
        }
    }

    //UC6
    // Ex: /pedidos/entregues?dataInicial=2023-01-01T00:00:00&dataFinal=2023-12-31T23:59:59
    @GetMapping("/entregues")
    @CrossOrigin("*")
    public ResponseEntity<?> listarEntregues(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal) {
        
        try {
            // Chamada ao Caso de Uso
            ListarPedidosEntreguesResponse response = listarPedidosEntreguesUC.run(dataInicial, dataFinal);            
            // Transformação para Presenter
            ListaPedidosPresenter presenter = ListaPedidosPresenter.fromEntityList(response.pedidos());
            return ResponseEntity.ok(presenter);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Erro ao listar os pedidos.");
        }
    }
}
