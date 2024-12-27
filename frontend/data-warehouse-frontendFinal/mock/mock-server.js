const chokidar = require('chokidar')
const bodyParser = require('body-parser')
const chalk = require('chalk')
const path = require('path')
const Mock = require('mockjs')
const { mocks } = require("./index");

const mockDir = path.join(process.cwd(), 'mock')

// Function to register routes dynamically
function registerRoutes(app) {
  let mockLastIndex
  const mocksForServer = mocks.map(route => {
    return responseFake(route.url, route.type, route.response)
  })

  for (const mock of mocksForServer) {
    app[mock.type](mock.url, mock.response)
    mockLastIndex = app._router.stack.length
  }

  const mockRoutesLength = Object.keys(mocksForServer).length
  return {
    mockRoutesLength: mockRoutesLength,
    mockStartIndex: mockLastIndex - mockRoutesLength
  }
}

// Function to unregister routes from the Express app
function unregisterRoutes() {
  Object.keys(require.cache).forEach(i => {
    if (i.includes(mockDir)) {
      delete require.cache[require.resolve(i)]
    }
  })
}

// Function for mocking API response
const responseFake = (url, type, respond) => {
  return {
    url: new RegExp(`${process.env.VUE_APP_BASE_API}${url}`),
    type: type || 'get',
    response(req, res) {
      console.log('Request invoked: ' + req.path) // Log the incoming request path
      res.json(Mock.mock(respond instanceof Function ? respond(req, res) : respond))
    }
  }
}

module.exports = app => {
  // Use bodyParser to parse JSON and URL-encoded data
  app.use(bodyParser.json())
  app.use(bodyParser.urlencoded({
    extended: true
  }))

  // Register mock routes
  const mockRoutes = registerRoutes(app)
  let mockRoutesLength = mockRoutes.mockRoutesLength
  let mockStartIndex = mockRoutes.mockStartIndex

  // Watch for file changes in the 'mock' directory to hot reload mock server
  chokidar.watch(mockDir, {
    ignored: /mock-server/,
    ignoreInitial: true
  }).on('all', (event, filePath) => {
    if (event === 'change' || event === 'add') {
      try {
        // Remove previous mock routes stack
        app._router.stack.splice(mockStartIndex, mockRoutesLength)

        // Clear the require cache for the updated mock files
        unregisterRoutes()

        // Register new mock routes
        const mockRoutes = registerRoutes(app)
        mockRoutesLength = mockRoutes.mockRoutesLength
        mockStartIndex = mockRoutes.mockStartIndex

        console.log(chalk.magentaBright(`\n > Mock Server hot reload success! Changed: ${filePath}`))
      } catch (error) {
        console.log(chalk.redBright('Error during hot reload: ', error))
      }
    }
  })

  // Log the port information when the server starts
  const port = process.env.PORT || 9528
  console.log(chalk.green(`\n > Mock server running at: http://localhost:${port}`))

  // Output the list of registered mock routes
  console.log(chalk.blue(` > Mock routes registered:`))
  mocks.forEach(route => {
    console.log(chalk.blue(` - ${route.type.toUpperCase()} ${route.url}`))
  })
}
