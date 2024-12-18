const { run } = require('runjs')
const chalk = require('chalk')
const config = require('../vue.config.js')
const rawArgv = process.argv.slice(2)
const args = rawArgv.join(' ')

if (process.env.npm_config_preview || rawArgv.includes('--preview')) {
  const report = rawArgv.includes('--report')

  run(`vue-cli-service build ${args}`)

  const port = 9528
  const publicPath = config.publicPath

  var connect = require('connect')
  var serveStatic = require('serve-static')
  const app = connect()

  app.use(
      publicPath,
      serveStatic('./dist', {
        index: ['index.html', '/']
      })
  )

  // Log the port to verify where the mock server is listening
  app.listen(port, function () {
    console.log(chalk.green(`> Mock server listening at http://localhost:${port}${publicPath}`))

    // If report is enabled, output the report URL
    if (report) {
      console.log(chalk.green(`> Report at http://localhost:${port}${publicPath}report.html`))
    }
  })
} else {
  run(`vue-cli-service build ${args}`)
}
