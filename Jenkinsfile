pipeline {
    agent {
        label "maven"
    }
    options { 
        skipDefaultCheckout()
        disableConcurrentBuilds()
    }
    stages {
        stage("Checkout Code") {
            steps {
                git branch: env.GIT_BRANCH, url: env.GIT_REPO
            }
        }
        stage("Compile Code") {
            steps {
                sh "mvn package -DskipTests"
            }
        }
        stage("Test Code") {
            steps {
                sh "mvn test"
            }
        }
        stage("Build Image") {
            steps {
                script {
                    openshift.withCluster {
                        openshift.withProject("dev") {
                            openshift.selector("bc", env.APP_NAME).startBuild("--from-dir=./target", "--wait=true");
                        }
                    }
                }
            }
        }
        stage("Tag Image") {
            steps {
                script {
                    openshift.withCluster {
                        openshift.withProject("dev") {
                            env.TAG = readMavenPom().getVersion()
                            openshift.tag("${APP_NAME}:latest", "${APP_NAME}:${TAG}")
                        }
                    }
                }
            }
        }
        stage("Deploy Application") {
            steps {
                script {
                    openshift.withCluster {
                        openshift.withProject("dev") {
                            openshift.set("triggers", "dc/${APP_NAME}", "--remove-all")
                            openshift.set("triggers", "dc/${APP_NAME}", "--from-image=${APP_NAME}:${TAG}", "-c ${APP_NAME}")
                            openshift.selector("dc", env.APP_NAME).rollout().status()
                        }
                    }
                }
            }
        }
        stage ('Validate OpenAPI Schema') {
            steps {
                microcksTest(server: 'minishiftMicrocks',
                    serviceId: "Moneda:${env.TAG}",
                    testEndpoint: 'http://backend-service-dev.192.168.64.6.nip.io/camel/api-doc',
                    runnerType: '', verbose: 'true')
            }
        }
    }
}