'use strict';

new Vue({
    el: '#app',
    data: {
        message: 'Hello Vue.js!',
        movies: {}
    },
    created: function () {
        this.getMovies()
    },
    methods: {
        getMovies: function () {
            let self = this;

            fetch('http://localhost:8080/movies')
                .then(response => response.json())
                .then(function (data) {
                    self.movies = data;
                })
        }
    }
});
