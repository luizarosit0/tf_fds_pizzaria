// Local: Adaptadores/Apresentacao/PedidoController.java
package com.luiza.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping; // Importar List
import org.springframework.web.bind.annotation.RestController;

import com.luiza.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.PedidoPresenter;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.Responses.SubmeterPedidoResponse;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoUC;
import com.luiza.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoUC.ItemData;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final SubmeterPedidoUC submeterPedidoUC;

    @Autowired
    public PedidoController(SubmeterPedidoUC submeterPedidoUC) {
        this.submeterPedidoUC = submeterPedidoUC;
    }

    // MUDANÇA 2: O endpoint agora é "/{clienteCpf}" e o método recebe parâmetros diferentes
    @PostMapping("/{clienteCpf}")
    @CrossOrigin("*")
    public ResponseEntity<?> submetePedido(
            @PathVariable String clienteCpf, 
            @RequestBody List<ItemData> itensData) {
        
        try {
            // MUDANÇA 3: Chamamos o caso de uso diretamente com os parâmetros recebidos
            SubmeterPedidoResponse response = submeterPedidoUC.run(clienteCpf, itensData);
            
            PedidoPresenter presenter = new PedidoPresenter(response.getPedido());

            return ResponseEntity.status(HttpStatus.CREATED).body(presenter);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao processar o pedido.");
        }
    }
}