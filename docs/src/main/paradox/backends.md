# Backends
Every checkpoint needs a backend to store metrics readings.

## Dropwizard
The [Dropwizard](http://metrics.dropwizard.io) backend creates the following metrics for each checkpoint. Note that each metric will be prefixed
with the checkpoint label you specify.

* `{label}_push_latency`: [histogram](dw-docs:/#histograms) to store the latencies between push and pull signals in nanoseconds
* `{label}_pull_latency`: [histogram](dw-docs:/#histograms) to store the latencies between pull and push signals in nanoseconds
* `{label}_backpressure_ratio`: [histogram](dw-docs:/#histograms) to store the percentage of time the checkpoint is backpressuring
* `{label}_throughput`: [meter](dw-docs://#meters) to store the throughput rate of the checkpoint

## Kamon
The [Kamon](https://kamon.io) backend creates the following metrics for each checkpoint. Note that each metric will be prefixed
with the checkpoint label you specify.

* `{label}_push_latency`: [histogram](kamon-docs:/core/basics/metrics/) to store the latencies between push and pull signals in nanoseconds
* `{label}_pull_latency`: [histogram](kamon-docs:/core/basics/metrics/) to store the latencies between pull and push signals in nanoseconds
* `{label}_backpressure_ratio`: [histogram](kamon-docs:/core/basics/metrics/) to store the percentage of time the checkpoint is backpressuring
* `{label}_throughput`: [counter](kamon-docs:/core/basics/metrics/) to store the throughput rate of the checkpoint

## Custom

TODO