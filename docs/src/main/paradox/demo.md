# Demo

## Setup Grafana

The demo needs a metrics server and a Grafana dashboard to connect to. 
The [Grafana/Graphite](https://hub.docker.com/r/kamon/grafana_graphite/) Docker image will give you everything you need. 

```
docker run -d -p 80:80 -p 81:81 -p 8125:8125/udp -p 8126:8126 -p 2003:2003 --name kamon-grafana-dashboard kamon/grafana_graphite
```

Once Grafana is up, navigate to `localhost:80`, login with credentials _admin:admin_ and import the JSON-encoded Grafana 
dashboard you find in `docs/test/resources/grafana`.

## Run it

Dropwizard
:   @@@vars
    ```
    sbt "docs/test:runMain com.example.demo.DropwizardDemo"
    ```
    @@@

Kamon
:   @@@vars
    ```
    sbt "docs/test:runMain com.example.demo.KamonDemo"
    ```
    @@@