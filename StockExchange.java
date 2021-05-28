package main.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class StockExchange {

    public static class Stock {
        private String orderId;
        private String stockName;
        private String orderType;
        private double amount;
        private long quantity;
        private String dateTime;

        public Stock(String orderId, String stockName, String orderType, double amount, long quantity, String dateTime) {
            this.orderId = orderId;
            this.stockName = stockName;
            this.orderType = orderType;
            this.amount = amount;
            this.quantity = quantity;
            this.dateTime = dateTime;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getStockName() {
            return stockName;
        }

        public void setStockName(String stockName) {
            this.stockName = stockName;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public long getQuantity() {
            return quantity;
        }

        public void setQuantity(long quantity) {
            this.quantity = quantity;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }
    }

    public static void main(String[] args) {

        System.out.println("Please enter the full path of the input file");

        Map<String, List<Stock>> buyMap = new LinkedHashMap<>();
        Map<String, List<Stock>> sellMap = new LinkedHashMap<>();

        Scanner in = new Scanner(System.in);

        String fileName = in.nextLine();

        File file = new File(fileName);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String input;
            while ((input = br.readLine()) != null) {
                String[] orderDetails = input.split(" ");
                if(orderDetails.length != 6) {
                    throw new Exception();
                }
                final Stock stock = new Stock(orderDetails[0], orderDetails[2], orderDetails[3],
                        Double.parseDouble(orderDetails[4]), Long.parseLong(orderDetails[5]), orderDetails[1]);
                if(stock.getOrderType().equals("sell")) {
                    if(!buyMap.containsKey(stock.getStockName())) {
                        if(!sellMap.containsKey(stock.getStockName())) {
                            final List<Stock> stocks = new ArrayList<>();
                            stocks.add(stock);
                            sellMap.put(stock.getStockName(), stocks);
                        }
                        else {
                            final List<Stock> stocks = sellMap.get(stock.getStockName());
                            stocks.add(stock);
                            sellMap.put(stock.getStockName(), stocks);
                        }
                    }
                    else {
                        matchSellOrder(stock, buyMap, sellMap);
                    }
                }
                if(orderDetails[3].equals("buy")) {
                    if(!sellMap.containsKey(stock.getStockName())) {
                        if(!buyMap.containsKey(stock.getStockName())) {
                            final List<Stock> stocks = new ArrayList<>();
                            stocks.add(stock);
                            buyMap.put(stock.getStockName(), stocks);
                        }
                        else {
                            final List<Stock> stocks = buyMap.get(stock.getStockName());
                            stocks.add(stock);
                            buyMap.put(stock.getStockName(), stocks);
                        }
                    }
                    else {
                        matchBuyOrder(stock, sellMap, buyMap);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Please enter a valid input file");
        }

    }

    private static void matchBuyOrder(final Stock stock,
                                      final Map<String, List<Stock>> sellMap,
                                      final Map<String, List<Stock>> buyMap) {
                        final List<Stock> stocks = sellMap.get(stock.getStockName());
                        for(int i=0; i<stocks.size(); i++) {
                            if(stocks.get(i).getAmount() <= stock.getAmount()) {
                                final long quantity = min(stock.getQuantity(), stocks.get(i).getQuantity());
                                System.out.println(stock.getOrderId() + " " + stocks.get(i).getAmount() + " " + quantity + " " + stocks.get(i).getOrderId());
                                if(stocks.get(i).getQuantity() == quantity) {
                                    stocks.remove(i);
                                    i--;
                                }
                                else if(stocks.get(i).getQuantity() > quantity) {
                                    final Stock currentStock = stocks.get(i);
                                    currentStock.setQuantity(currentStock.getQuantity() - quantity);
                                    stocks.set(i, currentStock);
                                }
                                if(stock.getQuantity() == quantity) {
                                    break;
                                }
                                else {
                                    stock.setQuantity(stock.getQuantity() - quantity);
                                }

                            }

                        }
                        if(stock.quantity != 0) {
                            if(!buyMap.containsKey(stock.getStockName())) {
                                final List<Stock> buyStocks = new ArrayList<>();
                                buyStocks.add(stock);
                                buyMap.put(stock.getStockName(), buyStocks);
                            }
                            else {
                                final List<Stock> buyStocks = buyMap.get(stock.getStockName());
                                buyStocks.add(stock);
                                buyMap.put(stock.getStockName(), buyStocks);
                            }
                        }
    }

    private static void matchSellOrder(final Stock stock,
                                       final Map<String, List<Stock>> buyMap,
                                       final Map<String, List<Stock>> sellMap) {
        final List<Stock> stocks = buyMap.get(stock.getStockName());
        for(int i=0; i<stocks.size(); i++) {
            if(stocks.get(i).getAmount() >= stock.getAmount()) {
                final long quantity = min(stock.getQuantity(), stocks.get(i).getQuantity());
                System.out.println(stocks.get(i).getOrderId() + " " + stock.getAmount() + " " + quantity + " " + stock.getOrderId());
                if(stocks.get(i).getQuantity() == quantity) {
                    stocks.remove(i);
                    i--;
                }
                else if(stocks.get(i).getQuantity() > quantity) {
                    final Stock currentStock = stocks.get(i);
                    currentStock.setQuantity(currentStock.getQuantity() - quantity);
                    stocks.set(i, currentStock);
                }
                if(stock.getQuantity() == quantity) {
                    break;
                }
                else {
                    stock.setQuantity(stock.getQuantity() - quantity);
                }
            }
        }

        if(stock.quantity != 0) {
            if(!sellMap.containsKey(stock.getStockName())) {
                final List<Stock> sellStocks = new ArrayList<>();
                sellStocks.add(stock);
                sellMap.put(stock.getStockName(), sellStocks);
            }
            else {
                final List<Stock> sellStocks = sellMap.get(stock.getStockName());
                sellStocks.add(stock);
                sellMap.put(stock.getStockName(), sellStocks);
            }
        }
    }

    private static long min(long a, long b) {
        if(a < b) {
            return a;
        }
        return b;
    }
}
