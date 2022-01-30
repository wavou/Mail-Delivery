var path = require('path');

module.exports = {
    entry: './src/main/js/index.js',
    devtool: 'sourcemaps',
    cache: true,
	mode: 'development',
    output: {
        path: __dirname,
        filename: './src/main/resources/static/built/bundle.js'
    },
	module: {
		rules: [
			{
				test: path.join(__dirname, '.'),
				exclude: /(node_modules)/,
				use: [{
					loader: 'babel-loader',
					options: {
						presets: ["@babel/preset-env", "@babel/preset-react"],
						plugins:[
							[
								"@babel/plugin-proposal-class-properties"
							]
						]
					}
				}]
			},
			{
				test: /\.css$/i,
				use: ['style-loader', 'css-loader'],
			},
			{
				test: /\.(png|gif|woff|woff2|eot|ttf|svg)$/,
				loader: "url-loader?limit=100000"
			}
		]
	}
};