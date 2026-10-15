(ns url-shortener.core
  (:require [url-shortener.server :as server]
            [url-shortener.cli :as cli])
  (:gen-class))

(defn -main [& args]
  (if (seq args)
    (server/start (Integer/parseInt (first args)))
    (cli/run)))
