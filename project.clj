(defproject todo-app "0.1.0-SNAPSHOT"
  :description "Todo learning app"
  :url "http://todo-app.com"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring "1.7.1"]
                 [ring/ring-json "0.4.0"]
                 [ring-cors "0.1.13"]
                 [org.clojure/data.json "0.2.6"]
                 [hiccup "1.0.5"]]
  :profiles {:dev {:dependencies [[ring/ring-mock "0.4.0"]
                                  [midje/midje "1.9.9"]
                                  [cljfmt "0.5.7"]]
                   :plugins [[lein-midje "3.0.0"]
                             [lein-cljfmt "0.6.6"]]}}
  :main todo-app.core
  :repl-options {:init-ns todo-app.core}
  :test-paths ["test/unit" "test/acceptance"])