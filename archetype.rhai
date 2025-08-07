let context = #{};

if switch_enabled("debug-answers") {
    log(Info, archetect::archetype::description() +" Answers:");
    display(as_yaml(ANSWERS));
}

// Use the same switches, defaults, etc passed to this archetype, and pass them to the next
let render_settings = archetect::archetype::settings();

context += render(Archetype("org-prompts"), ANSWERS, render_settings);

context += render(Archetype("java-project-attributes"), ANSWERS + context + #{
    "suffix_default": "Service",
    "suffix_options": [
        "Service",
        "Orchestrator",
        "Adapter",
    ],
}, render_settings);

context += prompt("Persistence:", "persistence", #{
  type: Select([
    "CockroachDB",
    "None",
  ]),
  defaults_with: "CockroachDB",
});

context += prompt("Artifactory Host:", "artifactory-host", #{
  defaults_with: "p6m.jfrog.io",
});

context += set("service-port", 9010);
context += set("management-port", context["service-port"] + 1);
context += set("database-port", context["service-port"] + 2);
context += set("debug-port", context["service-port"] + 9);

if ANSWERS.model != () {
  context.model = ANSWERS.model;
} else {
  context.model = default_model(context);
}

if switch_enabled("debug-context") {
    log(Info, `${archetect::archetype::description()} (${context["project-name"]}) Context:`);
    display(as_yaml(context));
}

render(Directory("contents/base"), context);

if context["persistence"] == "CockroachDB" {
  render(Directory("contents/persistence"), context);
  for entity_key in context.model.entities.keys() {
    let entity = context.model.entities[entity_key];
    let entity_context = #{};
    entity_context += set("entity-name", entity_key, #{
        cases: [
            CasedIdentityCasedValue(PROGRAMMING_CASES),
        ],
    });
    render(Directory("contents/persistence_entity"), context + entity_context);
  }
}

render(Archetype("manifests"), context["artifact-id"], context);
render(Archetype("gitignore"), context["artifact-id"], #{ "ignore": ["java"]});

if switch_enabled("zip") {
  zip(context["artifact-id"], render("{{ artifact-id }}.zip", context));
}

if switch_enabled("tarball") {
  tar_gz(context["artifact-id"], render("{{ artifact-id }}.tar.gz", context));
}

if switch_enabled("git-init") || switch_enabled("git-publish") {
  git_init(context["artifact-id"], "main");
  git_add_all(context["artifact-id"]);
  git_commit(context["artifact-id"], "initial commit");
  git_remote_add(context["artifact-id"], "origin", render("https://github.com/{{ org-solution-name }}/{{ artifact-id }}.git", context));
}

if switch_enabled("git-publish") {
  gh_repo_create(render("{{ org-solution-name }}/{{ artifact-id }}", context), Private);
  git_push(context["artifact-id"], "origin", "main");
}

fn default_model(context) {
    let yaml = `
---
entities:
  '${context["project-prefix"]}':
    fields:
      "id":
        type: "String"
        optional: false
      "name":
        type: "String"
        optional: false
`;
    let model = from_yaml(yaml);
    model
}


// Return Context
context
