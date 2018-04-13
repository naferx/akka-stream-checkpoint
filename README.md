# Experiments around monitoring an Akka Streams pipeline

## Dashboard
```
 docker run -d -p 80:80 -p 81:81 -p 8125:8125/udp -p 8126:8126 -p 2003:2003 --name kamon-grafana-dashboard kamon/grafana_graphite
```
More info [here](https://hub.docker.com/r/kamon/grafana_graphite/).

## Demo
```
sbt run
```
