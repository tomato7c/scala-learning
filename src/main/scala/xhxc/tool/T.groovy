package xhxc.tool

class T {

    static def json() {
        return new groovy.json.JsonSlurper()
    }
    static void main(String[] args) {
        String a = null
        def b = a?.toLong() ?: 0
        println b
    }
}
