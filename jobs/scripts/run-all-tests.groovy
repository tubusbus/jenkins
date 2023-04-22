timeout(60) {
    node('maven-slave') {
        stage('Checkout') {
            checkout scm
            //git branch: "$BRANCH", credentialsId: 'jenkins', url: 'git@github.com:saint88/jenkins.git'
        }
        stage('Run tests') {
            def jobs = [:]

            def runnerJobs = "$TEST_TYPE".split(",")

            jobs['ui-tests'] = {
                node('maven-slave') {
                    stage('Ui tests on chrome') {
                        if ('ui' in runnerJobs) {
                            catchError(buldResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                                build(job: 'ui-tests',
                                        parameters: [
                                                string(name: 'BRANCH', value: BRANCH),
                                                string(name: 'BASE_URL', value: BASE_URL),
                                                string(name: 'BROWSER', value: BROWSER),
                                                string(name: 'BROWSER_VERSION', value: BROWSER_VERSION),
                                                string(name: 'GRID_URL', value: GRID_URL)
                                        ])
                            }
                        } else {
                            echo 'Skipping stage...'
                            Utils.markStageSkippedForConditional('keystone api tests')
                        }
                    }
                }
            }
            parallel jobs
        }
    }
}