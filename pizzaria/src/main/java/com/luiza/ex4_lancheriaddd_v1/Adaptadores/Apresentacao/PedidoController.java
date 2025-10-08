package com.luiza.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import com.luiza.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.PedidoPresenter;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses.SubmeterPedidoResponse;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.ConsultarStatusUC;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoUC;
import com.luiza.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.ConsultarStatusUC;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.CancelarPedidoUC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final SubmeterPedidoUC submeterPedidoUC;
    private final ConsultarStatusUC consultarStatusUC;
    private final CancelarPedidoUC cancelarPedidoUC;


    @Autowired
    public PedidoController(SubmeterPedidoUC submeterPedidoUC, 
                            ConsultarStatusUC consultarStatusUC,
                            CancelarPedidoUC cancelarPedidoUC) {
        this.submeterPedidoUC = submeterPedidoUC;
        this.consultarStatusUC = consultarStatusUC;
        this.cancelarPedidoUC = cancelarPedidoUC;
    }

    @PostMapping("/submeter")
    public ResponseEntity<PedidoPresenter> submeterPedido(@RequestBody Pedido pedido) {
        int pedidosRecentes = 4; // Simulado
        
        SubmeterPedidoResponse response = submeterPedidoUC.run(pedido, pedidosRecentes);

        if (!response.isAprovado()) {
            return ResponseEntity.badRequest().build(); 
        }

        Pedido pedidoAprovado = response.getPedido();
        PedidoPresenter presenter = new PedidoPresenter(
            pedidoAprovado.getId(),
            pedidoAprovado.getStatus().toString(),
            pedidoAprovado.getSubtotal(),
            pedidoAprovado.getImpostos(),
            pedidoAprovado.getDesconto(),
            pedidoAprovado.getCustoFinal()
        );

        return ResponseEntity.ok(presenter);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<String> consultarStatus(@PathVariable(value="id") long id) {
        Pedido pedido = consultarStatusUC.run(id);

        if (pedido == null) {
            // Se o pedido não existe, retorna 404 Not Found
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pedido.getStatus().toString());
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<String> cancelarPedido(@PathVariable(value="id") long id) {
        boolean sucesso = cancelarPedidoUC.run(id);

        if (sucesso) {
            return ResponseEntity.ok("Pedido cancelado com sucesso.");
        }

        return ResponseEntity.badRequest().body("Pedido não encontrado ou não pode ser cancelado.");
    }
}