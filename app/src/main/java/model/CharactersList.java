package model;


import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "count",
        "next",
        "previous",
        "results"
})

public class CharactersList {

    @JsonProperty("count")
    private Integer count;
    @JsonProperty("next")
    private String next;
    @JsonProperty("previous")
    private Object previous;
    @JsonProperty("results")
    private List<Character> results = null;

    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(Integer count) {
        this.count = count;
    }

    @JsonProperty("next")
    public String getNext() {
        return next;
    }

    @JsonProperty("next")
    public void setNext(String next) {
        this.next = next;
    }

    @JsonProperty("previous")
    public Object getPrevious() {
        return previous;
    }

    @JsonProperty("previous")
    public void setPrevious(Object previous) {
        this.previous = previous;
    }

    @JsonProperty("results")
    public List<Character> getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(List<Character> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "CharactersList{" +
                "count=" + count +
                ", next='" + next + '\'' +
                ", previous=" + previous +
                ", results=" + results +
                '}';
    }
}
