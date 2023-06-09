#!/bin/bash

function help {
  echo "Usage: ./run.sh [OPTIONS] [ARGUMENTS]"
  echo ""
  echo "Arguments:"
  echo "  m, make       - Compile the code"
  echo "  r, run        - Run the code"
  echo "  mr, make-run  - Compile and run the code"
  echo "  -h, --help    - Show this help"
  echo ""
  echo "Options:"
  echo "  -p <problem>  - Specify the problem.pddl to run"
  echo "  -d <domain>    - Specify the domain.pddl to run"
  echo ""
  echo "Notes : By default, the problem and domain are for hanoi"
  echo ""
}

if [[ "$1" == "-h" || "$1" == "--help" ]]; then
	help
	exit 1
fi

parameter1=$2
arg1=$3
parameter2=$4
arg2=$5

domain="hanoi_domain.pddl"
problem="hanoi_problem.pddl"
path=$(pwd)
javaPath="./Planner/satPlanner/"

if [[ "$parameter1" == "-d" ]]; then
	domain=$arg1
fi
if [[ "$parameter1" == "-p" ]]; then
	problem=$arg1
fi
if [[ "$parameter2" == "-d" ]]; then
	domain=$arg2
fi
if [[ "$parameter2" == "-p" ]]; then
	problem=$arg2
fi


if [[ "$1" == "make" || "$1" == "m" ]]; then
	echo "[run.sh] * make"
	javac -d classes -cp lib/pddl4j-4.0.0.jar:lib/sat4j-sat.jar ${javaPath}*.java	
fi

if [[ "$1" == "run" || "$1" == "r" ]]; then
	echo "[run.sh] * run"
	java -cp classes:lib/pddl4j-4.0.0.jar:lib/sat4j-sat.jar ${javaPath}Main.java ${path}/${domain} ${path}/${problem}
fi

if [[ "$1" == "make-run" || "$1" == "mr" ]]; then
	echo "[run.sh] * make"
	javac -d classes -cp lib/pddl4j-4.0.0.jar:lib/sat4j-sat.jar ${javaPath}*.java
	echo "[run.sh] * run"	
	java -cp classes:lib/pddl4j-4.0.0.jar:lib/sat4j-sat.jar ${javaPath}Main.java ${path}/${domain} ${path}/${problem}
fi

