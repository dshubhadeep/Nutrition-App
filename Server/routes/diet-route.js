const router = require("express").Router();
const axios = require("axios");

router.get("/search/:id", (req, res) => {
	const query = req.params.id;
	const url = `https://api.edamam.com/api/food-database/parser?ingr=${query}&app_id=${
		process.env.APP_ID
	}&app_key=${process.env.APP_KEY}&page=0`;
	console.log(url);
	axios.get(url).then(response => {
		let data = response.data.hints;
		let suggestions = data.filter(item =>
			item.food.label.toLowerCase().includes(query)
		);
		if (suggestions.length) {
			res.json(suggestions);
		} else {
			res.json(data);
		}
	});
});

module.exports = router;
