# rarousnet

[![Build status](https://circleci.com/gh/rarous/rarousnet.svg?style=shield&circle-token=097382aa1befb3f0a98aa278ebd95b37858033a7)](https://circleci.com/gh/rarous/rarousnet)

## Build

```
lein uberjar
docker build -t rarousnet .
```

## Run

```
docker run -p 5000:5000 --rm -it rarousnet
```
