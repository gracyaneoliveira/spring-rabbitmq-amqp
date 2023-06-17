# Mensageria com Spring AMQP

**Aplicação desenvolvida no canal da Algaworks no [vídeo](https://www.youtube.com/watch?v=SzcvuHjRJKE). Uma introdução ao RabbitMQ.**

Três aplicações Spring:
- **order-service** (Api - produtor)
- **cashback-service** (backend simples - consumidor)
- **notification-service** (backend simples - consumidor)

As três aplicações irão se comunicar via mensageria. A **order-service** irá atuar como produtor e as outras aplicações serão consumidores.

**Run MySQL and RabbitMQ**
```
docker-compose up
```

**RabbitMQ Management**:
```
http://localhost:15672
user: rabbitmq
pass: rabbitmq
```

**Mensageria**

![Mensageria](comunicacao.png)

![Exchange](exchange.png)


