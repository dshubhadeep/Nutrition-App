const mongoose = require("mongoose");
const Schema = mongoose.Schema;
const bcrypt = require("bcryptjs");

const userSchema = new Schema({
	fullName: String,
	userName: String,
	password: String,
	age: Number,
	gender: String,
	height: Number,
	weight: Number,
	BMI: Number
});

userSchema.methods.comparePassword = function(enteredPwd) {
	console.log(this);
	return bcrypt.compareSync(enteredPwd, this.password);
};

userSchema.pre("save", function(next) {
	const SALT = 10;
	let myPwd = this.password;
	const hash = bcrypt.hashSync(myPwd, SALT);
	this.password = hash;
	this.BMI = parseFloat(
		(this.weight / Math.pow(this.height / 100, 2)).toFixed(1)
	);
	next();
});

module.exports = mongoose.model("user", userSchema);
