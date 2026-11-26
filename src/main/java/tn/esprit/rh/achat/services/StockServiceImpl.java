package tn.esprit.rh.achat.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.rh.achat.entities.Stock;
import tn.esprit.rh.achat.repositories.StockRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class StockServiceImpl implements IStockService {

	@Autowired
	StockRepository stockRepository;

	@Override
	public List<Stock> retrieveAllStocks() {
		log.info("In method retrieveAllStocks");
		List<Stock> stocks = (List<Stock>) stockRepository.findAll();
		for (Stock stock : stocks) {
			log.info(" Stock : {}", stock);
		}
		log.info("out of method retrieveAllStocks");
		return stocks;
	}

	@Override
	public Stock addStock(Stock s) {
		log.info("In method addStock");
		return stockRepository.save(s);
	}

	@Override
	public void deleteStock(Long stockId) {
		log.info("In method deleteStock");
		stockRepository.deleteById(stockId);
	}

	@Override
	public Stock updateStock(Stock s) {
		log.info("In method updateStock");
		return stockRepository.save(s);
	}

	@Override
	public Stock retrieveStock(Long stockId) {
		long start = System.currentTimeMillis();
		log.info("In method retrieveStock");
		Stock stock = stockRepository.findById(stockId).orElse(null);
		log.info("out of method retrieveStock");
		long elapsedTime = System.currentTimeMillis() - start;
		log.info("Method execution time: {} milliseconds.", elapsedTime);
		return stock;
	}

	@Override
	public String retrieveStatusStock() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date now = new Date();
		String msgDate = sdf.format(now);
		StringBuilder finalMessage = new StringBuilder();
		String newLine = System.lineSeparator();
		List<Stock> stocksEnRouge = stockRepository.retrieveStatusStock();
		for (Stock stock : stocksEnRouge) {
			finalMessage.append(newLine)
					.append(msgDate)
					.append(newLine)
					.append(": le stock ")
					.append(stock.getLibelleStock())
					.append(" a une quantité de ")
					.append(stock.getQte())
					.append(" inférieur à la quantité minimale a ne pas dépasser de ")
					.append(stock.getQteMin())
					.append(newLine);
		}
		String result = finalMessage.toString();
		log.info(result);
		return result;
	}

}
