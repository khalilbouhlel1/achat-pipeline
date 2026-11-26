package tn.esprit.rh.achat.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.rh.achat.entities.Stock;
import tn.esprit.rh.achat.services.IStockService;

import java.util.List;

@RestController
@Tag(name = "Gestion des stocks")
@RequestMapping("/stock")
@CrossOrigin("*")
public class StockRestController {

	@Autowired
	IStockService stockService;

	@GetMapping("/retrieve-all-stocks")
	public List<Stock> getStocks() {
		return stockService.retrieveAllStocks();
	}

	@GetMapping("/retrieve-stock/{stock-id}")
	public Stock retrieveStock(@PathVariable("stock-id") Long stockId) {
		return stockService.retrieveStock(stockId);
	}

	@PostMapping("/add-stock")
	public Stock addStock(@RequestBody Stock s) {
		return stockService.addStock(s);
	}

	@DeleteMapping("/remove-stock/{stock-id}")
	public void removeStock(@PathVariable("stock-id") Long stockId) {
		stockService.deleteStock(stockId);
	}

	@PutMapping("/modify-stock")
	public Stock modifyStock(@RequestBody Stock stock) {
		return stockService.updateStock(stock);
	}

}
