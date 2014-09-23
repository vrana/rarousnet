(defproject rarousnet "1.0.0-SNAPSHOT"
  :description "rarous.net web site"
  :url "http://rarous.net/"
  :license {:name "Apache License, Version 2.0"
            :url "https://www.apache.org/licenses/LICENSE-2.0.html"}
  :min-lein-version "2.0.0"
  :main rarousnet.web
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.logging "0.3.0"]
                 [clj-time "0.7.0"]
                 [compojure "1.1.8"]
                 [com.cognitect/transit-clj "0.8.259"]
                 [com.logentries/logentries-appender "1.1.25"]
                 [enlive "1.1.5"]
                 [environ "0.5.0"]
                 [http-kit "2.1.18"]
                 [log4j/log4j "1.2.17"]
                 [optimus "0.15.0"]
                 [ring "1.3.0"]
                 [ring/ring-defaults "0.1.0"]
                 [ring-basic-authentication "1.0.5"]
                 [ring.middleware.etag "1.0.0-SNAPSHOT"]
                 [bk/ring-gzip "0.1.1"]]
  :plugins [[lein-environ "0.5.0"]
            [lein-midje "3.1.3"]]
  :resource-paths ["src/rarousnet" "resources"]
  :profiles {:dev {:env {:production false}
                   :dependencies [[midje "1.6.3"]]}
             :production {:env {:production true}}})
