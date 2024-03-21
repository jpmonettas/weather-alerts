((clojure-mode . ((cider-clojure-cli-aliases . "1.12-storm:dev-tools:test")
				  (clojure-dev-menu-name . "weather-alerts-dev-menu")
				  (cider-jack-in-nrepl-middlewares . ("flow-storm.nrepl.middleware/wrap-flow-storm"
													  ("refactor-nrepl.middleware/wrap-refactor" :predicate cljr--inject-middleware-p)
													  "cider.nrepl/cider-middleware")))))
