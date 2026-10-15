(defproject url-shortener "0.1.0"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring/ring-core "1.9.6"]
                 [ring/ring-jetty-adapter "1.9.6"]
                 [korma "0.4.3"]
                 [org.xerial/sqlite-jdbc "3.36.0.3"]
                 [clj-http "3.12.3"]]
  :main url-shortener.core
  :aot :all)
