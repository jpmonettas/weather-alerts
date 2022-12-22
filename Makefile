.PHONY: test

test:
	clj -X:test cognitect.test-runner.api/test

flow-doc:
	clj -X:flow-doc:test
