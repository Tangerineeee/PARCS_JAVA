all: run

clean:
	rm -f out/Solver.jar out/FindNormMatrix.jar

out/Solver.jar: out/parcs.jar src/Solver.java
	@javac -cp /out/parcs.jar src/Solver.java
	@jar cf out/Solver.jar -C src Solver.class -C src
	@rm -f src/Solver.class

out/FindNormMatrix.jar: out/parcs.jar src/FindNormMatrix.java
	@javac -cp out/parcs.jar src/FindNormMatrix.java
	@jar cf out/FindNormMatrix.jar -C src FindNormMatrix.class -C src
	@rm -f src/FindNormMatrix.class

build: out/Solver.jar out/FindNormMatrix.jar

run: out/Solver.jar out/FindNormMatrix.jar
	@cd out && java -cp 'parcs.jar:Solver.jar' Solver
	
