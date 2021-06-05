;;; Author: David Goldfarb (deg@degel.com)
;;; Copyright (c) 2017-8, David Goldfarb

(defproject com.shark8me/re-frame-firebase "0.8.2"
  :description "A re-frame wrapper around firebase"
  :url "https://github.com/deg/re-frame-firebase"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure       "1.10.3"   :scope "provided"]
                 [org.clojure/clojurescript  "1.10.866"                  :scope "provided"
                  :exclusions [com.google.javascript/closure-compiler-unshaded
                               org.clojure/google-closure-library
                               org.clojure/google-closure-library-third-party]]
                 [thheller/shadow-cljs      "2.14.0"   :scope "provided"]
                 [reagent                   "1.0.0"]
                 [net.cgrand/macrovich      "0.2.1"]
                 [re-frame "1.2.0"]
                 [com.degel/iron "0.4.0"]
                 [org.clojure/tools.logging "1.1.0"]]
  
  :jvm-opts ^:replace ["-Xmx1g" "-server"]
  :plugins [[lein-shadow          "0.3.1"]]
  :profiles {:debug {:debug true}
             :dev   {:dependencies [[binaryage/devtools "1.0.3"]]
                     :plugins      [[lein-ancient       "0.6.15"]
                                    [lein-shell         "0.5.0"]]}}

  :clean-targets  [:target-path
                   "shadow-cljs.edn"
                   "node_modules"
                   "run/compiled"]

  :resource-paths ["resources"]
  :source-paths   ["src"]
  :test-paths     ["test"]

  :shell          {:commands {"karma" {:windows         ["cmd" "/c" "karma"]
                                       :default-command "karma"}
                              "open"  {:windows         ["cmd" "/c" "start"]
                                       :macosx          "open"
                                       :linux           "xdg-open"}}}

  :deploy-repositories [["clojars" {:sign-releases false
                                    :url "https://clojars.org/repo"
                                    :username :env/CLOJARS_USERNAME
                                    :password :env/CLOJARS_TOKEN}]]

  :release-tasks [["deploy" "clojars"]]
  :shadow-cljs {:nrepl  {:port 8777}

                :builds {:browser-test
                         {:target           :browser-test
                          :ns-regexp        "re-frame-firebase\\..*-test$"
                          :test-dir         "run/compiled/browser/test"
                          :compiler-options {:pretty-print                       true
                                             :external-config                    {:devtools/config {:features-to-install [:formatters :hints]}}}
                          :devtools         {:http-port 3449
                                             :http-root "run/compiled/browser/test"
                                             :preloads  [devtools.preload]}}

                         :karma-test
                         {:target           :karma
                          :ns-regexp        "re-frame\\..*-test$"
                          :output-to        "run/compiled/karma/test/test.js"
                          :compiler-options {:pretty-print                       true
                                             :closure-defines                    {re-frame.trace.trace-enabled? true}}}}}
  :aliases {"watch" ["do"
                     ["clean"]
                     ["shadow" "watch" "browser-test" "karma-test"]]

            "ci"    ["do"
                     ["clean"]
                     ["shadow" "compile" "karma-test"]]}
  )
