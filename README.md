
订阅地址 ws://127.0.0.1:8080/websocket/api

---
添加订阅：{"event":"addChannel","channel":"market.btc.cfd_detail"}

取消订阅：{"event":"removeChannel","channel":"market.btc.cfd_detail"}

发消息格式：{"channel":"market.btc.cfd_detail","message":"this is my message"}

---

添加订阅：{"event":"addChannel","channel":"market.BTC/USDT.detail"}

取消订阅：{"event":"removeChannel","channel":"market.BTC/USDT.detail"}

发消息格式：{"channel":"market.BTC/USDT.detail","message":"this is my message"}

---

订阅K线：{"event":"addChannel","channel":"market.63/69.kline.1min"}


发消息格式：{"channel":"market.63/69.kline.1min","message":"this is my message"}