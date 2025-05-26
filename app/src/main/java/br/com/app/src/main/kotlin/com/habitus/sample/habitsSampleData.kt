package br.com.app.src.main.kotlin.com.habitus.sample

import br.com.app.src.main.kotlin.com.habitus.data.entity.Days
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitEntity

val listOfCategories = listOf(
    "Desenvolvimento Pessoal",
    "Saúde e Bem-estar",
    "Organização e Produtividade",
    "Trabalho e Estudo",
    "Finanças Pessoais",
    "Relacionamentos e Vida Social",
    "Criatividade e Hobbies",
    "Autocuidado e Bem-estar Emocional",
    "Espiritualidade e Crescimento Espiritual",
)

val sampleHabits = listOf(
    HabitEntity(
        title = "Ler por 15 minutos",
        description = "Leia um livro, artigo ou qualquer conteúdo de interesse.",
        category = "Desenvolvimento Pessoal",
        pontuation = 10,
        days = listOf(Days.MONDAY.value, Days.FRIDAY.value),
        icon = "LineAwesomeIcons.BookOpen"
    ),
    HabitEntity(
        title = "Meditar 10 minutos",
        description = "Pratique mindfulness ou meditação guiada.",
        category = "Saúde e Bem-estar",
        pontuation = 8,
        days = listOf(Days.TUESDAY.value, Days.THURSDAY.value),
        icon = "LineAwesomeIcons.Brain"
    ),
    HabitEntity(
        title = "Beber 2L de água",
        description = "Hidrate-se bem durante o dia.",
        category = "Saúde e Bem-estar",
        pontuation = 5,
        days = listOf(Days.WEDNESDAY.value, Days.SATURDAY.value),
        icon = "LineAwesomeIcons.Tint"
    ),
    HabitEntity(
        title = "Fazer exercícios",
        description = "Faça qualquer tipo de exercício físico.",
        category = "Saúde e Bem-estar",
        pontuation = 12,
        days = listOf(Days.SUNDAY.value, Days.MONDAY.value, Days.WEDNESDAY.value),
        icon = "LineAwesomeIcons.Running"
    ),
    HabitEntity(
        title = "Anotar gastos do dia",
        description = "Registre todas as suas despesas.",
        category = "Finanças Pessoais",
        pontuation = 6,
        days = listOf(Days.TUESDAY.value, Days.THURSDAY.value, Days.SATURDAY.value),
        icon = "LineAwesomeIcons.MoneyBillWave"
    ),
    HabitEntity(
        title = "Planejar o dia seguinte",
        description = "Revise suas tarefas e organize o dia seguinte.",
        category = "Produtividade",
        pontuation = 7,
        days = listOf(Days.FRIDAY.value, Days.SUNDAY.value),
        icon = "LineAwesomeIcons.CalendarAlt"
    ),
    HabitEntity(
        title = "Enviar mensagem para um amigo",
        description = "Mantenha contato com quem importa.",
        category = "Relacionamentos",
        pontuation = 4,
        days = listOf(Days.MONDAY.value, Days.WEDNESDAY.value, Days.FRIDAY.value),
        icon = "LineAwesomeIcons.CommentDots"
    ),
    HabitEntity(
        title = "Estudar idioma por 30 minutos",
        description = "Aprenda ou pratique um novo idioma.",
        category = "Trabalho e Estudo",
        pontuation = 10,
        days = listOf(Days.TUESDAY.value, Days.THURSDAY.value),
        icon = "LineAwesomeIcons.Language"
    ),
    HabitEntity(
        title = "Escrever no diário",
        description = "Reflexão diária sobre o dia ou sentimentos.",
        category = "Desenvolvimento Pessoal",
        pontuation = 5,
        days = listOf(Days.SATURDAY.value, Days.SUNDAY.value),
        icon = "LineAwesomeIcons.PenFancy"
    ),
    HabitEntity(
        title = "Desconectar das redes por 1h",
        description = "Fique longe do celular e foque em você.",
        category = "Desenvolvimento Pessoal",
        pontuation = 8,
        days = listOf(Days.MONDAY.value, Days.WEDNESDAY.value, Days.FRIDAY.value),
        icon = "LineAwesomeIcons.MobileAlt"
    )
)
