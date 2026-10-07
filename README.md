# purescript-node-net

## JVM tests

`./bin/test` delegates to the [common runner](../javapurs/docs/testing.md#port-particulier) as `node-net`, but currently exits **1** with an unsupported-completion diagnostic: `Test.Main` returns after registering TCP server/socket callbacks.
`./bin/test --help` is read-only. Even with `--clean`, this unsupported protocol is rejected before build/workspace creation; the checkout and its outputs are preserved. See the [protocol inventory](../javapurs/docs/port-launchers.md).

[![Latest release](http://img.shields.io/github/release/purescript-node/purescript-node-net.svg)](https://github.com/purescript-node/purescript-node-net/releases)
[![Build status](https://github.com/purescript-node/purescript-node-net/workflows/CI/badge.svg?branch=master)](https://github.com/purescript-node/purescript-node-net/actions?query=workflow%3ACI+branch%3Amaster)
[![Pursuit](https://pursuit.purescript.org/packages/purescript-node-net/badge)](https://pursuit.purescript.org/packages/purescript-node-net)

A wrapper for Node's net API.

## Installation

```
spago install node-net
```

## Documentation

Module documentation is [published on Pursuit](http://pursuit.purescript.org/packages/purescript-node-net).
