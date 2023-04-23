timeout(60) {
    node('maven') {
        stage('Checkout') {
            checkout scm
        }
        stage('Run UI tests') {
            node('maven') {
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
            }
            parallel jobs
        }
    }
}