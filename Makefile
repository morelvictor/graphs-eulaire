.PHONY: build run clean

FILES := $(patsubst src/%,../src/%,$(shell find src | grep '\.java$$'))

build:
	-mkdir out
	cd out; javac $(FILES) -d .

run: build
	cd out; java App

clean:
	-rm -r out
