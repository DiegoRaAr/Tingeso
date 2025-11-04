package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.EmployeeEntity;
import com.example.tingeso1.entities.ToolEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ToolRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ToolRepository toolRepository;

    @Test
    public void whenFindToolByName_thenReturnTool() {
        //given
        ToolEntity tool = new ToolEntity(
                null,
                "Martillo",
                "Manual",
                10000,
                "ACTIVO",
                5,
                5000,
                2000,
                3000);
        entityManager.persistAndFlush(tool);

        //when
        ToolEntity found = toolRepository.findByNameTool("Martillo");

        //then
        assertThat(found.getNameTool()).isEqualTo("Martillo");

    }

    @Test
    public void whenFindByIdTool_thenReturnTool() {
        // given
        ToolEntity tool = new ToolEntity(null, "Taladro", "Eléctrica", 15000,
                "ACTIVO", 3, 8000, 2500, 4000);
        ToolEntity savedTool = entityManager.persistAndFlush(tool);

        // when
        ToolEntity found = toolRepository.findByIdTool(savedTool.getIdTool());

        // then
        assertThat(found).isNotNull();
        assertThat(found.getNameTool()).isEqualTo("Taladro");
    }

    @Test
    public void whenFindByCategoryTool_thenReturnMatchingTools() {
        // given
        ToolEntity t1 = new ToolEntity(null, "Martillo", "Manual", 10000,
                "ACTIVO", 5, 5000, 2000, 3000);
        ToolEntity t2 = new ToolEntity(null, "Destornillador", "Manual", 7000,
                "ACTIVO", 8, 3000, 1500, 2000);
        ToolEntity t3 = new ToolEntity(null, "Taladro", "Eléctrica", 20000,
                "ACTIVO", 2, 10000, 4000, 5000);

        entityManager.persist(t1);
        entityManager.persist(t2);
        entityManager.persist(t3);
        entityManager.flush();

        // when
        var result = toolRepository.findByCategoryTool("Manual");

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCategoryTool()).isEqualTo("Manual");
    }

    @Test
    public void whenFindByStateTool_thenReturnActiveTools() {
        // given
        ToolEntity t1 = new ToolEntity(null, "Sierra", "Manual", 12000,
                "ACTIVO", 4, 6000, 2500, 3500);
        ToolEntity t2 = new ToolEntity(null, "Lima", "Manual", 5000,
                "BAJA", 1, 2000, 1000, 1500);

        entityManager.persist(t1);
        entityManager.persist(t2);
        entityManager.flush();

        // when
        var result = toolRepository.findByStateTool("ACTIVO");

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStateTool()).isEqualTo("ACTIVO");
    }

    @Test
    public void whenFindByDailyCharge_thenReturnToolsWithSameCharge() {
        // given
        ToolEntity t1 = new ToolEntity(null, "Cincel", "Manual", 8000,
                "ACTIVO", 2, 1500, 4000, 2000);
        ToolEntity t2 = new ToolEntity(null, "Broca", "Eléctrica", 10000,
                "ACTIVO", 3, 2000, 4000, 3000);
        ToolEntity t3 = new ToolEntity(null, "Taladro", "Eléctrica", 20000,
                "ACTIVO", 1, 4000, 10000, 5000);


        entityManager.persist(t1);
        entityManager.persist(t2);
        entityManager.persist(t3);
        entityManager.flush();

        // when
        var result = toolRepository.findByDailyCharge(4000);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getDailyCharge()).isEqualTo(4000);
    }

}
