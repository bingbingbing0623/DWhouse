const tokens = {
  admin: {
    token: 'admin-token'
  },
  editor: {
    token: 'editor-token'
  }
}

const users = {
  'admin-token': {
    roles: ['admin'],
    introduction: 'I am a super administrator',
    avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
    name: 'Super Admin'
  },
  'editor-token': {
    roles: ['editor'],
    introduction: 'I am an editor',
    avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
    name: 'Normal Editor'
  }
}

module.exports = [
  // user login
  {
    url: '/data-warehouse/user/login',
    type: 'post',
    response: config => {
      // 输出所有请求信息
      console.log('Login request received:');
      console.log('Request Body:', config.body);
      console.log('Request Headers:', config.headers);
      console.log('Request Query:', config.query);

      const { username } = config.body;
      const token = tokens[username];

      // mock error
      if (!token) {
        console.log('Error: Token not found for username:', username);
        return {
          code: 60204,
          message: 'Account and password are incorrect.'
        };
      }

      console.log('Login successful for username:', username, 'Returning token:', token);
      return {
        code: 20000,
        data: token
      };
    }
  },

  // get user info
  {
    url: '/data-warehouse/user/info*',
    type: 'get',
    response: config => {
      console.log('Get user info request received:');
      console.log('Request Query:', config.query);
      console.log('Request Headers:', config.headers);

      const { token } = config.query;
      const info = users[token];

      // mock error
      if (!info) {
        console.log('Error: User info not found for token:', token);
        return {
          code: 50008,
          message: 'Login failed, unable to get user details.'
        };
      }

      console.log('User info found for token:', token, 'Returning data:', info);
      return {
        code: 20000,
        data: info
      };
    }
  },

  // user logout
  {
    url: '/data-warehouse/user/logout',
    type: 'post',
    response: _ => {
      console.log('Logout request received');
      return {
        code: 20000,
        data: 'success'
      };
    }
  }
]
